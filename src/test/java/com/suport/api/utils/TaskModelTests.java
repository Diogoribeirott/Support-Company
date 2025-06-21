package com.suport.api.utils;

import java.util.HashSet;
import java.util.Set;

import com.suport.api.domain.Task;
import com.suport.api.dtos.request.TaskRequestCreateDTO;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;

public class TaskModelTests {

    public static Task createtaskValid() {
      return Task.builder()
            .title("Login system error")
            .description("The Client reported that they are unable to access their account.")
            .status(TaskStatus.CLOSED)
            .priority(TaskPriority.HIGH)
            .client(ClientModelTest.createClientValidWithAddress())
            .technicians(new HashSet<>(Set.of(TechnicianModelTest.createtechnicianValid())))
            .build();
    }
    
    public static TaskRequestCreateDTO taskRequestDTO(Long clientID, long technicianid) {
         return new TaskRequestCreateDTO (
        "Draven",
         "The Client reported that they are unable to access their account2.",
        TaskStatus.CLOSED,
        TaskPriority.HIGH,
        clientID,
         Set.of(technicianid)
        );
      
    }

    public static TaskResponseDTO taskResponseDTO() {
      return new TaskResponseDTO(
        1L,
        "Login system error",
        "The Client reported that they are unable to access their account.",
        TaskStatus.CLOSED,
        TaskPriority.HIGH,
        1l,
        new HashSet<>(Set.of(2l))
      );
    }

    


}
