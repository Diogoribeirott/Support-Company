package com.suport.api.controller;

import com.suport.api.domain.Client;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.request.ClientRequestUpdateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.mappers.ClientMapper;
import com.suport.api.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // =============================
    // CREATE
    // =============================
    @Operation(
        summary = "Create a new client",
        description = "Creates and persists a new client in the database"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Client created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDTO.class)
                
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ClientResponseDTO> save(@RequestBody @Valid ClientRequestCreateDTO body) {
        ClientResponseDTO response = clientService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================
    // READ (BY ID)
    // =============================
    @Operation(
        summary = "Get client by ID",
        description = "Returns the client identified by the given ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Client found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Client not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
        Client client = clientService.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok(ClientMapper.toResponseDTO(client));
    }

    // =============================
    // READ (ALL)
    // =============================
    @Operation(
        summary = "List all clients",
        description = "Returns a list of all clients in the database"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Clients retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ClientResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> findAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    // =============================
    // UPDATE
    // =============================
    @Operation(
    summary = "Update client by ID",
    description = "Updates the client identified by the provided ID and with the provided data  in body",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Client data to update",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ClientRequestUpdateDTO.class),
            examples = {
                @ExampleObject(
                    name = "Client with Address",
                    description = "Client data including address",
                    value = """
                            {
                              "name": "AWS",
                              "email": "AWS@example.com",
                              "taxId": "98765432100",
                              "phone": "(11) 91234-5678",
                              "type": "BUSINESS",
                              "address": {
                                    "street": "Flowers Street",
                                    "number": "123",
                                    "neighborhood": "Downtown",
                                    "city": "São Paulo",
                                    "state": "SP",
                                    "postalCode": "01234-000"
                                }

                            }
                            """
                ),
                @ExampleObject(
                    name = "Client without Address",
                    description = "Client data without address",
                    value = """
                            {
                              "name": "Google",
                              "email": "Google@example.com",
                              "taxId": "12345678900",
                              "phone": "(21) 99876-5432",
                              "type": "BUSINESS"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Client updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Client not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@RequestBody @Valid ClientRequestUpdateDTO body, @PathVariable Long id) {
        return ResponseEntity.ok(clientService.update(body, id));
    }

    // =============================
    // DELETE
    // =============================
    @Operation(
        summary = "Delete client by ID",
        description = "Deletes the client identified by the given ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Client deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Client not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
