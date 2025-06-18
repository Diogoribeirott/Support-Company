package com.suport.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressResponseDTO(

    @Schema(description = "Client id", example = "1")
    Long id,

    @Schema(description = "Street name", example = "Main Street")
    String street,

    @Schema(description = "House or building number", example = "123")
    String number,

    @Schema(description = "State (UF)", example = "SP")
    String state,

    @Schema(description = "City name", example = "Los Angeles")
    String city

) {}
