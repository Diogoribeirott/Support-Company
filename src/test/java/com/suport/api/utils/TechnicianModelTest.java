package com.suport.api.utils;

import org.springframework.http.ResponseEntity;

import com.suport.api.domain.Technician;

public class TechnicianModelTest {

    public static Technician createtechnicianValid(){
      return Technician.builder()
        .name("Cleitinho")
        .phone("(00) 0000-0000")
        .build();
       
    }

    public static Technician updateTechnicianValid(){
      return Technician.builder()
        .name("Draven")
        .phone("(77) 0000-0000")
        .build();
       
    }
    public static Technician technicianResponseDTO(){
      return Technician.builder()
        .id(1L)
        .name("Draven")
        .phone("(77) 0000-0000")
        .build();
       
    }

       public static Technician technicianResquestDTO(){
      return Technician.builder()
        .name("Draven")
        .phone("(77) 0000-0000")
        .build();
       
    }
       

}
