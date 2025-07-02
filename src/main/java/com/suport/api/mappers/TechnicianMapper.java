package com.suport.api.mappers;

import com.suport.api.domain.Technician;
import com.suport.api.dtos.response.TechnicianResponseDTO;

public class TechnicianMapper {
    
    public static TechnicianResponseDTO toResponseDTO(Technician technician){
      return  new  TechnicianResponseDTO(
            technician.getId(),
            technician.getName(),
            technician.getPhone()
      );
    }

}
