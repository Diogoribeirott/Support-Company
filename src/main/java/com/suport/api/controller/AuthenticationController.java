package com.suport.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suport.api.config.TokenService;
import com.suport.api.domain.UserModel;
import com.suport.api.dtos.request.AuthenticationDTO;
import com.suport.api.dtos.request.RegisterDTO;
import com.suport.api.dtos.response.LoginResponseDTO;
import com.suport.api.repository.UserModelRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private UserModelRepository userModelRepository;

    private TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(
        AuthenticationManager authenticationManager,
        UserModelRepository userModelRepository,
        TokenService tokenService
        ){
        this.authenticationManager = authenticationManager;
        this.userModelRepository = userModelRepository;
        this.tokenService = tokenService;

    }

    // =============================
    // LOGIN
    // =============================

    @Operation(
        summary = "Login",
        description = "Authenticates a user and returns a JWT Token."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data (e.g. missing login or password).",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content = @Content
        )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){

        logger.info("Login attempt for user {}", data.login());
        var usernamePassword  = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.createToken((UserModel) auth.getPrincipal());
        logger.info("Login successful for user {}", data.login());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    // =============================
    // REGISTER NEW USER
    // =============================
    
    @Operation(
        summary = "Register",
        description = "Register a new user.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Register successful.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data (e.g. missing login or password).",
            content = @Content
        ),
         @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Unauthorized: access denied due to invalid credentials or expired/invalid token.",
            content = @Content
        ),
         @ApiResponse(
            responseCode = "409",
            description = "Conflict: username already exists.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content = @Content
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data){
         logger.info("Register attempt for user {}", data.login());

        if(userModelRepository.findByLogin(data.login()).isPresent()){

            logger.warn("Registration failed: username {} already exists", data.login());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists " +data.login());
        }

          String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
          UserModel userModel = new UserModel(data.login(), encryptedPassword, data.role());
        
        userModelRepository.save(userModel);
        logger.info("User created successfully: {}", data.login());

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }

}
