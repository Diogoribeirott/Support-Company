package com.suport.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private UserModelRepository userModelRepository;

    private TokenService tokenService;

    public AuthenticationController(
        AuthenticationManager authenticationManager,
        UserModelRepository userModelRepository,
        TokenService tokenService
        ){
        this.authenticationManager = authenticationManager;
        this.userModelRepository = userModelRepository;
        this.tokenService = tokenService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword  = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

         String token = tokenService.createToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data){

        if(userModelRepository.findByLogin(data.login()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists " +data.login());
        }

          String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
          UserModel userModel = new UserModel(data.login(), encryptedPassword, data.role());
        
        userModelRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }

}
