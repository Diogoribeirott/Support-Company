package com.suport.api.utils;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;

public class AddressTests {

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
    

    public static AddressRequestDTO createAddressDTOValid(){
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
}
