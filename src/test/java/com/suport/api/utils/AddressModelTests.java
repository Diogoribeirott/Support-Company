package com.suport.api.utils;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;

public class AddressModelTests {

    public static Address createAddressValid(){
        return Address.builder()
        .street("Main Street")
        .number("123")
        .state("California")
        .city("Los Angeles")
        .postalCode("90001")
        .complement("Apt 45")
        .district("Downtown" )
        .build();

    }
    
    public static Address createAddressValidWithId(){
        return Address.builder()
        .id(5l)
        .street("Main Street")
        .number("123")
        .state("California")
        .city("Los Angeles")
        .postalCode("90001")
        .complement("Apt 45")
        .district("Downtown" )
        .build();

    }
    

    public static AddressRequestDTO createAddressResquestDTOValid(){
        return new AddressRequestDTO( 
            "Main Street",     
            "123",             
            "California",     
            "Los Angeles",     
            "90001",            
            "Apt 45",           
            "Downtown"          
        );

    }
    public static AddressRequestDTO createAddressDTOValidUpdate(){
        return new AddressRequestDTO( 
            "ALPHENEIROS",     
            "4",             
            "California",     
            "Los Angeles",     
            "90001",            
            "Apt 45",           
            "Downtown"          
        );

    }

     public static AddressResponseDTO createAddressResponseDTO(){
        return new AddressResponseDTO(
            1l, 
            "Main Street", 
            "123", 
            "SP", 
            "Los Angeles");

    }
}
