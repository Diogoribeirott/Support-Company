package com.suport.api.dtos.response;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

public record TechnicianResponseDTO(

    @Schema(description = "Technician id", example = "1")
    Long id,

    @Schema(description = "Technician name", example = "David")
    String name,
    
    @Schema(description = "Phone number", example = "(00) 0000-0000")
    String phone,

    @Schema(description = "List of Task IDs associated with the Technician", example = "[101, 102]")
    Set<Long> tasksIds
) {
} 
