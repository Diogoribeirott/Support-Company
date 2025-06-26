package com.suport.api.dtos.request;

import com.suport.api.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record RegisterDTO(

    @NotBlank
    @Size(min = 5, max = 50, message = "Login must be between 5 and 50 characters")
    @Schema(description = "Username to register", example = "Draven22", required = true)
    String login,

    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User password", example = "MySecret123", required = true)
    String password,

    @NotNull
    @Schema(description = "Role of the user being registered", example = "ADMIN", required = true)
    UserRole role

) {}
