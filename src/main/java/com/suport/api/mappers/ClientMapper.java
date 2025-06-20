package com.suport.api.mappers;

import java.util.stream.Collectors;

import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.dtos.response.ClientResponseDTO;

public class ClientMapper {
    
    public static ClientResponseDTO toResponseDTO(Client client) {
        AddressResponseDTO addressResponseDTO = null;
    
    if (client.getAddress() != null) {
        addressResponseDTO = AddressMapper.createAddressResponseDTO(client.getAddress());
    }

        return new ClientResponseDTO(
            client.getId(),
            client.getName(),
            client.getEmail(),
            client.getTaxId(),
            client.getPhone(),
            addressResponseDTO,
            client.getType()
        );
    }

   
}
