package com.suport.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
    
     @Schema(
        description = "JWT token to be used for accessing secured endpoints",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VyIn0.9ZqNnKO-jFuEXAMPLE"
    )
    String token
    ) {}
