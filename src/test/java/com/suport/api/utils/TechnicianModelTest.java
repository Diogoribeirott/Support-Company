package com.suport.api.utils;

import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;

public class TechnicianModelTest {

  public static Technician technicianValid2(){
      return Technician.builder()
        .name("Draven")
        .phone("(00) 0000-0000")
        .build();
       
    }

    public static Technician technicianValid(){
      return Technician.builder()
        .id(1l)
        .name("Draven")
        .phone("(00) 0000-0000")
        .build();
       
    }

    public static Technician updateTechnicianValid(){
      return Technician.builder()
        .name("Draven")
        .phone("(00) 0000-0000")
        .build();
       
    }
    public static TechnicianResponseDTO technicianResponseDTO(){
      return new TechnicianResponseDTO(1L, "Draven", "(00) 0000-0000");
    }

       public static TechnicianRequestDTO technicianResquestDTO(){
      return new TechnicianRequestDTO("Draven", "(00) 0000-0000");
    }
       

}
