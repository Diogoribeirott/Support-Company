package com.suport.api.controller;

import com.suport.api.domain.Address;
import com.suport.api.dtos.AddressDTO;
import com.suport.api.service.AddressService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;     
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
        summary = "Create a new address",
        description = "Creates and persists a new address in the database",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Address data to be saved",
            required = true
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Address created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Address.class)
            )
        ),
        @ApiResponse(responseCode = "400",
            description = "Invalid input data", 
            content = @Content
            ),
        @ApiResponse(responseCode = "500",
            description = "Internal server error",
            content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Address> save(@RequestBody @Valid AddressDTO body){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(body));
    }

    @Operation(
        summary = "Get address by ID",
        description = "Get the address identified by the given ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Address found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Address.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Address> findID(@PathVariable Long id){
        return ResponseEntity.ok().body(addressService.findByIdOrThrowBadRequestException(id));
    }

    @Operation(
        summary = "Get list all address",
        description = "Get list of all addresses in database"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Addresses found successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Address.class))
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<Address>> findAll(){
        return ResponseEntity.ok().body(addressService.findAll());
    }

    @Operation(
        summary = "Update an existing address by its ID",
        description = "Updates the address identified by the given ID using the data provided in the request body (AddressDTO). " +
                      "Returns the updated address if successful."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Address updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Address.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@RequestBody @Valid AddressDTO body, @PathVariable Long id){
        return ResponseEntity.ok().body(addressService.update(body, id));
    }

    @Operation(
        summary = "Delete an existing address by its ID",
        description = "Delete the address identified by the given ID using the data provided and Returns the updated address if successful."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Address delete successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Address not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
