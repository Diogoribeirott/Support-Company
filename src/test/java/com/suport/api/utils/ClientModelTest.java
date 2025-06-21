package com.suport.api.utils;

import java.util.Set;
import java.util.stream.Collectors;

import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.enums.ClientType;

public class ClientModelTest {

//     {
//   "name": "Google",
//   "email": "Example@gmail.com",
//   "taxId": "12.345.678/0001-00 or 123.456.789-00 ",
//   "phone": "(00) 0000-0000",
//   "address": {
//     "street": "Main Street",
//     "number": "123",
//     "state": "SP",
//     "city": "Los Angeles",
//     "postalCode": "01000-000",
//     "complement": "Apt 101",
//     "district": "Downtown"
//   },
//   "type": "INDIVIDUAL"
// }
    public static Client createClientValidWithAddress(){
        return Client.builder()
         .name("Google")
         .email("Example@gmail.com")
         .taxId("12.345.678/0001-00")
         .phone("(00) 0000-0000")
         .type(ClientType.BUSINESS)
         .address(AddressModelTests.createAddressValid())
         .build();
         
    }
      public static Client updateClientValidWithAddress(){
        return Client.builder()
         .name("League of leguends")
         .email("lol@gmail.com")
         .taxId("12.345.678/0001-00")
         .phone("(00) 0000-0000")
         .type(ClientType.INDIVIDUAL)
         .address(AddressModelTests.createAddressValid())
         .build();
         
    }

    public static Client createClientValidWithoutAddress(){
       return Client.builder()
    .name("Google")
    .email("Example@gmail.com")
    .taxId("12.345.678/0001-00")
    .phone("(00) 0000-0000")
    .type(ClientType.BUSINESS)
    .address(AddressModelTests.createAddressValid())
    .build();
    }

    public static ClientRequestCreateDTO createClientResquestDTO(){
        return new ClientRequestCreateDTO(
            "Google",
            "Example@gmail.com",
            "12.345.678/0001-00",
            "(00) 0000-0000",
            AddressModelTests.createAddressResquestDTOValid() ,
            ClientType.BUSINESS);
         
    }

    public static ClientResponseDTO createClientResponseDTO(){
        return new ClientResponseDTO(
            1l, 
            "Google",
            "Example@gmail.com",
            "12.345.678/0001-00",
            "(00) 0000-0000",
            AddressModelTests.createAddressResponseDTO() ,
            ClientType.BUSINESS);

        }
    }
