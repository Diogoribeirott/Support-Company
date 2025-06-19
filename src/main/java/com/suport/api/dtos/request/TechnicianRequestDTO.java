package com.suport.api.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TechnicianRequestDTO(

    @NotBlank(message = "Technician name cannot be null or empty.")
    @Schema(description = "Technician name", example = "David")
    String name,

    @Schema(description = "Phone number", example = "(00) 0000-0000")
    String phone

) {
} 


