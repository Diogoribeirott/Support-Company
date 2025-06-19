package com.suport.api.mappers;

import java.util.stream.Collectors;

import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
import com.suport.api.dtos.response.TechnicianResponseDTO;

public class TechnicianMapper {
    
    public static TechnicianResponseDTO toResponseDTO(Technician technician){
      return  new  TechnicianResponseDTO(
            technician.getId(),
            technician.getName(),
            technician.getPhone(),
            technician.getTasks().stream()
            .map(Task::getId)
            .collect(Collectors.toSet())
         
      );
    }

}
