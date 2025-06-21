package com.suport.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.service.AddressService;
import com.suport.api.utils.AddressModelTests;

@ExtendWith(SpringExtension.class)
public class AddressControllerTest {

     @InjectMocks
     private AddressController addressController;

    @Mock
    private AddressService addressServiceMock;

    private Address addressValidWithId;

    @BeforeEach
    void setUp(){

        addressValidWithId = AddressModelTests.createAddressValidWithId();
        AddressResponseDTO addressResponseDTO = AddressModelTests.createAddressResponseDTO();

        BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
            .thenReturn(addressValidWithId);

        BDDMockito.when(addressServiceMock.findAll())
            .thenReturn(List.of(addressResponseDTO));

        BDDMockito.when(addressServiceMock.save(ArgumentMatchers.any(AddressRequestDTO.class)))
            .thenReturn(addressResponseDTO);
        
        BDDMockito.when(addressServiceMock.update(ArgumentMatchers.any(AddressRequestDTO.class),ArgumentMatchers.any(Long.class)))
            .thenReturn(addressResponseDTO);

        BDDMockito.doNothing().when(addressServiceMock).deleteById(ArgumentMatchers.any(Long.class));

    }


    @Test
    @DisplayName("Delete by id: delete address by id when successful ")
    void delete_deleteAddressAndReturnNoContent_when_Sucessful() {
        ResponseEntity<Void> response = addressController.deleteById(1l);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Find by id: find address by id when successful ")
    void findById_ReturnAnAddress_when_sucessful() {
         ResponseEntity<AddressResponseDTO> response = addressController.findById(1l);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

         AddressResponseDTO addressResponseDTO = response.getBody();

        Assertions.assertThat(addressResponseDTO).isNotNull();
        Assertions.assertThat(addressResponseDTO.id()).isNotNull();
        Assertions.assertThat(addressResponseDTO.street()).isEqualTo(addressValidWithId.getStreet());
        Assertions.assertThat(addressResponseDTO.number()).isEqualTo(addressValidWithId.getNumber());
        Assertions.assertThat(addressResponseDTO.city()).isEqualTo(addressValidWithId.getCity());
        Assertions.assertThat(addressResponseDTO.state()).isEqualTo(addressValidWithId.getState());

    }

    @Test
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
            .thenThrow(new BadRequestException("Address not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->addressController.findById(5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");

    }

    @Test
    @DisplayName("FindALL: return list of addreses")
    void findAll_ReturnListOfAddress_when_sucessful() {
         ResponseEntity<List<AddressResponseDTO>> response = addressController.findAll();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        List<AddressResponseDTO> addressResponseDTOList = response.getBody();

        Assertions.assertThat(addressResponseDTOList).isNotEmpty().isNotNull();
       Assertions.assertThat(addressResponseDTOList)
          .anyMatch(dto -> 
              dto.street().equals(addressValidWithId.getStreet()) &&
              dto.city().equals(addressValidWithId.getCity()) &&
              dto.number().equals(addressValidWithId.getNumber()) 
          );
    }

    @Test
    @DisplayName("Save: save addressDTO and return an address")
    void save_returnAddress_when_sucessfull() {

        ResponseEntity<AddressResponseDTO> response = addressController.save( AddressModelTests.createAddressResquestDTOValid());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

         AddressResponseDTO addressResponseDTO = response.getBody();

        Assertions.assertThat(addressResponseDTO).isNotNull();
        Assertions.assertThat(addressResponseDTO.id()).isNotNull();
      
    }

    @Test
    @DisplayName("Update: update address with addressDTO and long id, return an address")
    void update_returnAddress_when_sucessfull() {
         AddressResponseDTO addressResponseDTO = addressController.update(AddressModelTests.createAddressResquestDTOValid(), 5l).getBody();

        Assertions.assertThat(addressResponseDTO).isNotNull();
        Assertions.assertThat(addressResponseDTO.id()).isNotNull();
    }

    @Test
    @DisplayName("when address with id does not exist: throw bad request exception ")
    void update_ReturnthrowBadRequestException_when_idNotExits() {
        BDDMockito.when(addressServiceMock.update(ArgumentMatchers.any(AddressRequestDTO.class),ArgumentMatchers.any(Long.class)))
            .thenThrow(new BadRequestException("Address not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->addressController.update(AddressModelTests.createAddressResquestDTOValid(),5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");

    }
}
