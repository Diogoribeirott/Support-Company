package com.suport.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDTO(

    @NotBlank(message = "Street cannot be null or empty")
    @Size(min = 3, message = "Street must have at least 3 characters")
    String street,

    @NotBlank(message = "Number cannot be null or empty")
    String number,

    @NotBlank(message = "State cannot be null or empty")
    @Size(min = 3, message = "State must have at least 3 characters")
    String state,

    @NotBlank(message = "City cannot be null or empty")
    @Size(min = 3, message = "City must have at least 3 characters")
    String city,

    String postalCode,

    String complement,

    String district
) {}
