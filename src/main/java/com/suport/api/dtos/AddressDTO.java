package com.suport.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(

    @Size(min = 3, message = "Street must have at least 3 characters")
    @Schema(description = "Street name", example = "Main Street")
    @NotBlank(message = "Street cannot be null or empty")
    String street,

    @Schema(description = "House or building number", example = "123")
    @NotBlank(message = "Number cannot be null or empty")
    String number,

    @Size(min = 2, message = "State must have at least 2 characters")
    @NotBlank(message = "State cannot be null or empty")
    @Schema(description = "State (UF)", example = "SP")
    String state,

    @Size(min = 3, message = "City must have at least 3 characters")
    @Schema(description = "City name", example = "Los Angeles")
    @NotBlank(message = "City cannot be null or empty")
    String city,

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Postal code must be in format 00000-000")
    @Schema(description = "Postal code (CEP)", example = "01000-000")
    String postalCode,

    @Schema(description = "Additional address info", example = "Apt 101")
    String complement,

    @Schema(description = "District or neighborhood", example = "Downtown")
    String district
) {}
