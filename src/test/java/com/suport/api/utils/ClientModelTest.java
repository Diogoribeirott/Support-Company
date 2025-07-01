package com.suport.api.utils;

import java.util.Set;
import java.util.stream.Collectors;

import com.suport.api.domain.Address;
import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.request.ClientRequestUpdateDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.enums.ClientType;

public class ClientModelTest {

    public static Client clientValid() {
        return new Client().builder()
        .id(1l)
        .name("Draven")
        .email("Draven@gmail.com")
        .taxId("123.456.789-00")
        .phone("00 0000-0000")
        .type(ClientType.INDIVIDUAL)
        .build();
    }

     public static ClientResponseDTO clientResponseDTO() {
         return new ClientResponseDTO(1l,
        "Draven",
        "Draven@gmail.com",
        "123.456.789-00", 
        "00 0000-0000", 
        null, 
        ClientType.INDIVIDUAL);
     }

      public static ClientRequestCreateDTO clientRequestCreateDTO() {
         return new ClientRequestCreateDTO(
        "Draven",
        "Draven@gmail.com",
        "123.456.789-00", 
        "00 0000-0000", 
        null, 
        ClientType.INDIVIDUAL);
     }

       public static ClientRequestUpdateDTO clientRequestUpdateDTO() {
         return new ClientRequestUpdateDTO("Draven", 
         "Draven@gmail.com", 
         "123.456.789-00", 
         "00 0000-0000", 
         null, 
         ClientType.INDIVIDUAL, 
         "central", 
         "parker");
     }
     
}
