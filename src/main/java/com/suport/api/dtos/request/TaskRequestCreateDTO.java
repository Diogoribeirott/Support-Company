package com.suport.api.dtos.request;

import java.util.Set;

import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequestCreateDTO(

    @Size(min = 3, message = "Name must have at least 3 characters.")
    @Schema(description = "Task title ", example = "Printer with printing problem")
    @NotBlank(message = "Task title cannot be null or empty.")
    String title,

    @Schema(description = "Problem description", example = "when I try to print more than 2 sheets I get an error")
    String description,

    @Schema(description = "Status of the task. Valid values: OPEN, IN_PROGRESS, COMPLETED, CLOSED", example = "OPEN")
    @NotNull(message = "TaskStatus cannot be null or empty.")
    TaskStatus status,

    @Schema(description = "Priority of the task. Valid values: LOW, MEDIUM, HIGH, URGENT", example = "MEDIUM")
    @NotNull(message = "TaskStatus cannot be null or empty.")
    TaskPriority priority,

    @Schema(description = "The ID of the client that called the task", example = "1")
    @NotNull(message = "ID client cannot be null or empty.")
    Long clientId, 

    @Schema(description = "Receives technician IDs in (format Set)", example = "[2,7]")
    Set<Long> technicianIds

) {}
