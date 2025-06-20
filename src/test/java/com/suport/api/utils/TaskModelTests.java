package com.suport.api.utils;

import java.util.HashSet;
import java.util.Set;

import com.suport.api.domain.Task;
import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;

public class TaskModelTests {

    public static Task createtaskValid() {
      return Task.builder()
            .title("Login system error")
            .description("The Client reported that they are unable to access their account.")
            .status(TaskStatus.OPEN)
            .priority(TaskPriority.HIGH)
            .client(ClientModelTest.createClientValidWithAddress())
            .technicians(new HashSet<>(Set.of(TechnicianModelTest.createtechnicianValid())))
            .build();
    }

}
