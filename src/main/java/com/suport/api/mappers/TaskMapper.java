package com.suport.api.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
import com.suport.api.dtos.response.TaskResponseDTO;

public class TaskMapper {

    public static TaskResponseDTO toResponseDTO(Task task) {
    Long clientId = task.getClient() != null ? task.getClient().getId() : null;

    Set<Long> technicianIds = task.getTechnicians() != null
        ? task.getTechnicians().stream()
            .map(Technician::getId)
            .collect(Collectors.toSet())
        : Set.of();

       return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            clientId,
            technicianIds
        
        );
        
    }

}
