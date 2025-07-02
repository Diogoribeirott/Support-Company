package com.suport.api.dtos.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record TechnicianResponseDTO(

    @Schema(description = "Technician id", example = "1")
    Long id,

    @Schema(description = "Technician name", example = "David")
    String name,
    
    @Schema(description = "Phone number", example = "(00) 0000-0000")
    String phone

) {
} 
