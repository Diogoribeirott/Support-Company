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
    void setUp() {
        addressValidWithId = AddressModelTests.createAddressValidWithId();
        AddressResponseDTO addressResponseDTO = AddressModelTests.createAddressResponseDTO();

        BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
                .thenReturn(addressValidWithId);

        BDDMockito.when(addressServiceMock.findAll())
                .thenReturn(List.of(addressResponseDTO));

        BDDMockito.when(addressServiceMock.save(ArgumentMatchers.any(AddressRequestDTO.class)))
                .thenReturn(addressResponseDTO);

        BDDMockito.when(addressServiceMock.update(
                ArgumentMatchers.any(AddressRequestDTO.class),
                ArgumentMatchers.any(Long.class)))
                .thenReturn(addressResponseDTO);

        BDDMockito.doNothing().when(addressServiceMock).deleteById(ArgumentMatchers.any(Long.class));
    }

    // ----------------------------------------
    // DELETE
    // ----------------------------------------

    @Test
    @DisplayName("Delete by ID: should delete address and return NO_CONTENT")
    void deleteById_ShouldReturnNoContent_whenSuccessful() {
        ResponseEntity<Void> response = addressController.deleteById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // ----------------------------------------
    // FIND BY ID
    // ----------------------------------------

    @Test
    @DisplayName("Find by ID: should return address when successful")
    void findById_ShouldReturnAddress_whenSuccessful() {
        ResponseEntity<AddressResponseDTO> response = addressController.findById(1L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AddressResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
        Assertions.assertThat(body.street()).isEqualTo(addressValidWithId.getStreet());
        Assertions.assertThat(body.number()).isEqualTo(addressValidWithId.getNumber());
        Assertions.assertThat(body.city()).isEqualTo(addressValidWithId.getCity());
        Assertions.assertThat(body.state()).isEqualTo(addressValidWithId.getState());
    }

    @Test
    @DisplayName("Find by ID: should throw BadRequestException when not found")
    void findById_ShouldThrowBadRequestException_whenNotExists() {
        BDDMockito.when(addressServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
                .thenThrow(new BadRequestException("Address not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> addressController.findById(5L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");
    }

    // ----------------------------------------
    // FIND ALL
    // ----------------------------------------

    @Test
    @DisplayName("Find All: should return list of addresses")
    void findAll_ShouldReturnListOfAddresses_whenSuccessful() {
        ResponseEntity<List<AddressResponseDTO>> response = addressController.findAll();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<AddressResponseDTO> body = response.getBody();

        Assertions.assertThat(body).isNotNull().isNotEmpty();
        Assertions.assertThat(body).anyMatch(dto ->
                dto.street().equals(addressValidWithId.getStreet()) &&
                dto.city().equals(addressValidWithId.getCity()) &&
                dto.number().equals(addressValidWithId.getNumber())
        );
    }

    // ----------------------------------------
    // SAVE
    // ----------------------------------------

    @Test
    @DisplayName("Save: should save address and return AddressResponseDTO")
    void save_ShouldReturnAddress_whenSuccessful() {
        ResponseEntity<AddressResponseDTO> response =
                addressController.save(AddressModelTests.createAddressResquestDTOValid());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AddressResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
    }

    // ----------------------------------------
    // UPDATE
    // ----------------------------------------

    @Test
    @DisplayName("Update: should update and return AddressResponseDTO")
    void update_ShouldReturnAddress_whenSuccessful() {
        ResponseEntity<AddressResponseDTO> response =
                addressController.update(AddressModelTests.createAddressResquestDTOValid(), 5L);

        AddressResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: should throw BadRequestException when address not found")
    void update_ShouldThrowBadRequestException_whenNotExists() {
        BDDMockito.when(addressServiceMock.update(
                ArgumentMatchers.any(AddressRequestDTO.class),
                ArgumentMatchers.any(Long.class)))
                .thenThrow(new BadRequestException("Address not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> addressController.update(AddressModelTests.createAddressResquestDTOValid(), 5L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");
    }
}
