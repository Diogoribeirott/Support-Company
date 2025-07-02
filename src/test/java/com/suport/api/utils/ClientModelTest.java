package com.suport.api.utils;


import com.suport.api.domain.Address;
import com.suport.api.domain.Client;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.request.ClientRequestUpdateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.enums.ClientType;

public class ClientModelTest {

   public static Address createAddressValid(){
        return Address.builder()
        .street("Main Street")
        .number("123")
        .state("California")
        .city("Los Angeles")
        .postalCode("13025-200")
        .complement("Apt 45")
        .district("Downtown" )
        .build();

    }


  public static Client clientValid2() {
    return Client.builder()
    .address(addressValid())
    .name("Draven")
    .email("Draven@gmail.com")
    .taxId("123.456.789-00")
    .phone("00 0000-0000")
    .build();
}

    public static Client clientValid() {
    return Client.builder()
    .id(1L)
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
         addressRequestDTOValid(), 
        ClientType.INDIVIDUAL);
     }

 

       public static ClientRequestUpdateDTO clientRequestUpdateDTO() {
         return new ClientRequestUpdateDTO("Draven", 
         "Draven@gmail.com", 
         "123.456.789-00", 
         "00 0000-0000", 
          addressRequestDTOValid(), 
         ClientType.INDIVIDUAL, 
         "central", 
         "parker");
     }

       public static ClientRequestCreateDTO clientRequestCreateDTO2() {
         return new ClientRequestCreateDTO(
        "Draven",
        "Draven@gmail.com",
        "123.456.789-00", 
        "00 0000-0000", 
        addressRequestDTOValid(), 
        ClientType.INDIVIDUAL);
     }
     
     public static AddressRequestDTO addressRequestDTOValid() {
    return new AddressRequestDTO(
        "Main Street",     // street (min 3, not blank)
        "123",             // number (not blank)
        "SP",              // state (min 2, not blank)
        "São Paulo",       // city (min 3, not blank)
        "01000-000",       // postalCode (regex \d{5}-\d{3})
        "Apt 101",         // complement (optional)
        "Centro"           // district (optional)
    );
}

public static Address addressValid() {
    return Address.builder()
        .street("Main Street")
        .number("123")
        .state("SP")
        .city("São Paulo")
        .postalCode("01000-000")
        .complement("Apto 101")
        .district("Centro")
        .build();
}
}
