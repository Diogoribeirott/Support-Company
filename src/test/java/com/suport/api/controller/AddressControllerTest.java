package com.suport.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.service.AddressService;
import com.suport.api.utils.AddressTests;

@ExtendWith(SpringExtension.class)
public class AddressControllerTest {

    // @InjectMocks
    // private AddressController addressController;

    // @Mock
    // private AddressService addressServiceMock;

    // @BeforeEach
    // void setUp(){
    //     Address addressValidWithId = AddressTests.createAddressValidWithId();
    //     List<Address> listAddresses = List.of(addressValidWithId);

    //     BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
    //         .thenReturn(addressValidWithId);

    //     BDDMockito.when(addressServiceMock.findAll())
    //         .thenReturn(listAddresses);

    //     BDDMockito.when(addressServiceMock.save(ArgumentMatchers.any(AddressDTO.class)))
    //         .thenReturn(addressValidWithId);
        
    //     BDDMockito.when(addressServiceMock.update(ArgumentMatchers.any(AddressDTO.class),ArgumentMatchers.any(Long.class)))
    //         .thenReturn(addressValidWithId);

    //     BDDMockito.doNothing().when(addressServiceMock).deleteById(ArgumentMatchers.any(Long.class));

    // }


    // @Test
    // @DisplayName("Delete by id: delete address by id when successful ")
    // void delete_deleteAddressAndReturnNoContent_when_Sucessful() {
    //     ResponseEntity<Void> response = addressController.deleteById(1l);

    //     Assertions.assertThat(response).isNotNull();
    //     Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    // }

    // @Test
    // @DisplayName("Find by id: find address by id when successful ")
    // void findById_ReturnAnAddress_when_sucessful() {

    //     Address addressValidWithId = AddressTests.createAddressValidWithId();
    //     ResponseEntity<Address> response = addressController.findID(5l);

    //     Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    //     Address address = response.getBody();

    //     Assertions.assertThat(address).isNotNull();
    //     Assertions.assertThat(address.getId()).isNotNull();
    //     Assertions.assertThat(address.getStreet()).isEqualTo(addressValidWithId.getStreet());
    //     Assertions.assertThat(address.getNumber()).isEqualTo(addressValidWithId.getNumber());
    //     Assertions.assertThat(address.getCity()).isEqualTo(addressValidWithId.getCity());
    //     Assertions.assertThat(address.getState()).isEqualTo(addressValidWithId.getState());
    //     Assertions.assertThat(address.getPostalCode()).isEqualTo(addressValidWithId.getPostalCode());
    //     Assertions.assertThat(address.getComplement()).isEqualTo(addressValidWithId.getComplement());
    //     Assertions.assertThat(address.getDistrict()).isEqualTo(addressValidWithId.getDistrict());

    // }

    // @Test
    // @DisplayName("when id does not exist: throw bad request exception ")
    // void findById_ReturnthrowBadRequestException_when_idNotExits() {

    //     BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
    //         .thenThrow(new BadRequestException("Address not found"));

    //         BadRequestException exception = assertThrows(BadRequestException.class, () ->addressController.findID(5l));

    //         Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");

    // }

    // @Test
    // @DisplayName("FindALL: return list of addreses")
    // void findAll_ReturnListOfAddress_when_sucessful() {

    //     Address addressValidWithId = AddressTests.createAddressValidWithId();
    //     ResponseEntity<List<Address>> response = addressController.findAll();

    //     Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    //     List<Address> listAddresses = response.getBody();

    //     Assertions.assertThat(listAddresses).isNotEmpty();
    //     Assertions.assertThat(listAddresses.get(0)).isEqualTo(addressValidWithId);
    // }

    // @Test
    // @DisplayName("Save: save addressDTO and return an address")
    // void save_returnAddress_when_sucessfull() {

    //     AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();

    //     ResponseEntity<Address> response = addressController.save(addressDTOValid);

    //     Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    //     Address address = response.getBody();

    //     Assertions.assertThat(address).isNotNull();
    //     Assertions.assertThat(address.getId()).isNotNull();
      
    // }

    // @Test
    // @DisplayName("Update: update address with addressDTO and long id, return an address")
    // void update_returnAddress_when_sucessfull() {
    //     AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();
    //     Address address = addressController.update(addressDTOValid, 5l).getBody();

    //     Assertions.assertThat(address).isNotNull();
    //     Assertions.assertThat(address.getId()).isNotNull();

    // }

    // @Test
    // @DisplayName("when address with id does not exist: throw bad request exception ")
    // void update_ReturnthrowBadRequestException_when_idNotExits() {
    //     AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();
    //     BDDMockito.when(addressServiceMock.update(ArgumentMatchers.any(AddressDTO.class),ArgumentMatchers.any(Long.class)))
    //         .thenThrow(new BadRequestException("Address not found"));

    //         BadRequestException exception = assertThrows(BadRequestException.class, () ->addressController.update(addressDTOValid,5l));

    //         Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");

    // }
}
