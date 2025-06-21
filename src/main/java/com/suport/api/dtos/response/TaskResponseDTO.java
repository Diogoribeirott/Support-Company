package com.suport.api.dtos.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskResponseDTO(

    @Schema(description = "Task ID ", example = "2")
    Long id,

    @Schema(description = "Task title ", example = "Printer with printing problem")
    String title,

    @Schema(description = "Problem description", example = "when I try to print more than 2 sheets I get an error")
    String description,

    @Schema(description = "Status of the task. Valid values: OPEN, IN_PROGRESS, COMPLETED, CLOSED", example = "OPEN")
    TaskStatus status,

    @Schema(description = "Priority of the task. Valid values: LOW, MEDIUM, HIGH, URGENT", example = "MEDIUM")
    TaskPriority priority,

    @Schema(description = "The ID of the client that called the task", example = "1")
    Long clientId, 

    @Schema(description = "Receives technician IDs in (format Set)", example = "[2,7]")
    Set<Long> technicianIds

) {}
