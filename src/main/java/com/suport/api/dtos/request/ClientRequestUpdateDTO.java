package com.suport.api.dtos.request;

import com.suport.api.enums.ClientType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientRequestUpdateDTO(

    @Size(min = 3, message = "Name must have at least 3 characters.")
    @Schema(description = "Client name", example = "Google")
    @NotBlank(message = "Client name cannot be null or empty.")
    String name,

    @Email(message = "Email format is not valid.")
    @Schema(description = "Client Email ", example = "Example@gmail.com")
    @NotBlank(message = "Email cannot be null or empty.")
    String email,

    @Size(min = 9, message = "TaxId must have at least 9 characters.")
    @Schema(description = "CNPJ or CPF client", example = "12.345.678/0001-00 or 123.456.789-00 ")
    @NotBlank(message = "CNPJ or CPF cannot be null or empty.")
    String taxId,

    @Schema(description = "Phone number", example = "(00) 0000-0000")
    String phone,

    @Schema(description = "Client address.")
    AddressRequestDTO address,

    @NotNull(message = "ClientType cannot be null.")
    @Schema(description = "Type of client: INDIVIDUAL or COMPANY", example = "INDIVIDUAL")
    ClientType type,

    @Schema(description = "Additional address info", example = "Apt 101")
    String complement,

    @Schema(description = "District or neighborhood", example = "Downtown")
    String district
) {

}

