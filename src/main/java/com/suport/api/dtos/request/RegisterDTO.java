package com.suport.api.dtos.request;

import com.suport.api.enums.UserRole;

import jakarta.validation.constraints.NotBlank;


public record RegisterDTO(

    @NotBlank
    String login,
    
    @NotBlank
    String password,

    UserRole role

) {}
