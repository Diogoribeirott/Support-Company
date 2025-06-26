package com.suport.api.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO(

    @NotBlank(message = "Login is required")
    @Size(min = 5, message = "Login must have at least 5 characters")
    @Schema(
        description = "The user's login (username). Must be at least 5 characters long.",
        example = "Draven22"
    )
    String login, 

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Schema(
        description = "The user's password. Must be at least 8 characters long.",
        example = "superSecret123"
    )
    String password
    
    ) {}
