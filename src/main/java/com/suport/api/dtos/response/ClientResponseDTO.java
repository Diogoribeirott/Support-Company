package com.suport.api.dtos.response;

import java.util.Set;

import com.suport.api.enums.ClientType;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClientResponseDTO(

    @Schema(description = "Client id", example = "1")
    Long id,

    @Schema(description = "Client name", example = "Google")
    String name,

    @Schema(description = "Client Email ", example = "Example@gmail.com")
    String email,

    @Schema(description = "CNPJ or CPF client", example = "12.345.678/0001-00 or 123.456.789-00 ")
    String taxId,

    @Schema(description = "Phone number", example = "(00) 0000-0000")
    String phone,

    @Schema(description = "Client address.")
    AddressResponseDTO address,

    @Schema(description = "Type of client: INDIVIDUAL or COMPANY", example = "INDIVIDUAL")
    ClientType type,

    @Schema(description = "List of Task IDs associated with the client", example = "[101, 102]")
    Set<Long> tasksIds

){}
