package com.suport.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suport.api.domain.Task;
import com.suport.api.dtos.request.TaskRequestCreateDTO;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.mappers.TaskMapper;
import com.suport.api.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/tasks")
public class TaskController {

     private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    // =============================
    // CREATE
    // =============================
    @Operation(
        summary = "Get a new Task",
        description = "Created and persists in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created Task with sucessfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDTO.class )
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
    }
    )
    @PostMapping
    public ResponseEntity<TaskResponseDTO> save(@RequestBody TaskRequestCreateDTO dto){
        TaskResponseDTO response = taskService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // =============================
    // READ (BY ID)
    // =============================
    @Operation(
        summary = "Get Task by ID",
        description = "Returns the Task identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Task found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDTO.class)
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
     public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id){
         Task task = taskService.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok().body(TaskMapper.toResponseDTO(task));
    }

    // =============================
    // READ (ALL)
    // =============================
    @Operation(
        summary = "List all Task",
        description = "Returns a list of all Task in the database",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Task retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class))
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
     public ResponseEntity<List<TaskResponseDTO>> findAll(){
        return ResponseEntity.ok().body(taskService.findAll());
    }

    // =============================
    // UPDATE
    // =============================
    @Operation(
    summary = "Update client by ID",
    description = "Updates the Task identified by the provided ID and with the provided data  in body",
        security = @SecurityRequirement(name = "bearerAuth"),
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Task data to update",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TaskRequestCreateDTO.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Task updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TaskRequestCreateDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Task not found",
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
    public ResponseEntity<TaskResponseDTO> update(@RequestBody TaskRequestCreateDTO dto,@PathVariable Long id){
        return ResponseEntity.ok().body( taskService.update(dto, id));
    }

    // =============================
    // DELETE
    // =============================
     @Operation(
        summary = "Delete Task by ID",
        description = "Deletes the Task identified by the given ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Task deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Task not found",
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
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
