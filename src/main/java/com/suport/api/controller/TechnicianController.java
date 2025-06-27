package com.suport.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.mappers.TechnicianMapper;
import com.suport.api.service.TechnicianService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService){
        this.technicianService = technicianService;
    }

    // =============================
    // CREATE
    // =============================
    @Operation(
        summary = "Get a new Technician",
        description = "Created and persists in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created Technician with sucessfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TechnicianResponseDTO.class )
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
    }
    )
    @PostMapping
    public ResponseEntity<TechnicianResponseDTO> save(@RequestBody TechnicianRequestDTO dto){
        TechnicianResponseDTO response = technicianService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // =============================
    // READ (BY ID)
    // =============================
    @Operation(
        summary = "Get Technician by ID",
        description = "Returns the Technician identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Technician found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TechnicianResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Client not found",
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
     public ResponseEntity<TechnicianResponseDTO> findById(@PathVariable Long id){
         Technician technician = technicianService.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok().body(TechnicianMapper.toResponseDTO(technician));
    }

    // =============================
    // READ (ALL)
    // =============================
    @Operation(
        summary = "List all Technician",
        description = "Returns a list of all Technician in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Technician retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TechnicianResponseDTO.class))
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
     public ResponseEntity<List<TechnicianResponseDTO>> findAll(){
        return ResponseEntity.ok().body(technicianService.findAll());
    }

    // =============================
    // UPDATE
    // =============================
    @Operation(
    summary = "Update client by ID",
    description = "Updates the Technician identified by the provided ID and with the provided data  in body",
        security = @SecurityRequirement(name = "bearerAuth"),
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Technician data to update",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TechnicianRequestDTO.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Technician updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TechnicianRequestDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Technician not found",
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
    public ResponseEntity<TechnicianResponseDTO> update(@RequestBody TechnicianRequestDTO dto,@PathVariable Long id){
        return ResponseEntity.ok().body( technicianService.update(dto, id));
    }

    // =============================
    // DELETE
    // =============================
     @Operation(
        summary = "Delete Technician by ID",
        description = "Deletes the Technician identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Technician deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Technician not found",
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
    public ResponseEntity<Void> delete(@PathVariable Long id){
        technicianService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
