package com.suport.api.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.repository.AddressRepository;
import com.suport.api.utils.AddressModelTests;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AddressControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AddressRepository addressRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("FindALL: return list of addresses")
    void findAll_ReturnListOfAddress_when_successful() {
        Address savedAddress = addressRepository.save(AddressModelTests.createAddressValid());

        ResponseEntity<List<AddressResponseDTO>> response = testRestTemplate.exchange(
                "/addresses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<AddressResponseDTO> addresses = response.getBody();
        Assertions.assertThat(addresses).isNotEmpty();
        Assertions.assertThat(addresses.get(0).city()).isEqualTo(savedAddress.getCity());
    }

    @Test
    @DisplayName("Find by id: find address by id when successful")
    void findById_ReturnAnAddress_when_successful() {
        Address savedAddress = addressRepository.save(AddressModelTests.createAddressValid());

        String url = "/addresses/" + savedAddress.getId();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AddressResponseDTO address = response.getBody();
        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.id()).isNotNull();
        Assertions.assertThat(address.street()).isEqualTo(savedAddress.getStreet());
        Assertions.assertThat(address.number()).isEqualTo(savedAddress.getNumber());
        Assertions.assertThat(address.city()).isEqualTo(savedAddress.getCity());
        Assertions.assertThat(address.state()).isEqualTo(savedAddress.getState());
    }

    @Test
    @DisplayName("When id does not exist: throw bad request exception")
    void findById_ReturnThrowBadRequestException_when_idNotExists() {
        String url = "/addresses/9999999";

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: save addressDTO and return an address")
    void save_ReturnAddress_when_successful() {
        AddressRequestDTO requestDTO = AddressModelTests.createAddressResquestDTOValid();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                "/addresses",
                HttpMethod.POST,
                new HttpEntity<>(requestDTO),
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AddressResponseDTO savedAddress = response.getBody();
        Assertions.assertThat(savedAddress).isNotNull();
        Assertions.assertThat(savedAddress.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: update address with addressDTO and long id, return an address")
    void update_ReturnAddress_when_successful() {
        Address existingAddress = addressRepository.save(AddressModelTests.createAddressValid());
        existingAddress.setStreet("alpheneiros");

        String url = "/addresses/" + existingAddress.getId();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(existingAddress),
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AddressResponseDTO updatedAddress = response.getBody();
        Assertions.assertThat(updatedAddress).isNotNull();
        Assertions.assertThat(updatedAddress.id()).isNotNull();
        Assertions.assertThat(updatedAddress.street()).isEqualTo("alpheneiros");
    }

    @Test
    @DisplayName("Delete by id: delete address by id when successful")
    void delete_DeleteAddressAndReturnNoContent_when_successful() {
        Address savedAddress = addressRepository.save(AddressModelTests.createAddressValid());

        String url = "/addresses/" + savedAddress.getId();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        boolean exists = addressRepository.existsById(savedAddress.getId());

        Assertions.assertThat(exists).isFalse();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
