package com.suport.api.mappers;

import com.suport.api.domain.Address;
import com.suport.api.dtos.response.AddressResponseDTO;

public class AddressMapper {
    
 public static AddressResponseDTO createAddressResponseDTO(Address address){
        return new AddressResponseDTO(
        address.getId(), 
        address.getStreet(), 
        address.getNumber(), 
        address.getState(), 
        address.getCity()
    );
    }
}
