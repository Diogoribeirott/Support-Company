package com.suport.api.controller;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.mappers.AddressMapper;
import com.suport.api.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // =============================
    // CREATE
    // =============================
     @Operation(
        summary = "Create a new address",
        description = "Creates and persists a new address in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Address created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<AddressResponseDTO> save(@RequestBody @Valid AddressRequestDTO body) {
        AddressResponseDTO response = addressService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================
    // READ (BY ID)
    // =============================
    @Operation(
        summary = "Get address by ID",
        description = "Returns the address identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Address found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findById(@PathVariable Long id) {
        Address address = addressService.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok(AddressMapper.createAddressResponseDTO(address));
    }

    // =============================
    // READ (ALL)
    // =============================
    @Operation(
        summary = "List all addresses",
        description = "Returns a list of all addresses in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Addresses retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> findAll() {
        return ResponseEntity.ok(addressService.findAll());
    }

    // =============================
    // UPDATE
    // =============================
    @Operation(
        summary = "Update address by ID",
        description = "Updates the address identified by the given ID with the data provided",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Address updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@RequestBody @Valid AddressRequestDTO body, @PathVariable Long id) {
        return ResponseEntity.ok(addressService.update(body, id));
    }

    // =============================
    // DELETE
    // =============================
    @Operation(
        summary = "Delete address by ID",
        description = "Deletes the address identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Address deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
         @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid credentials or invalid/expired token.",
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
        addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
