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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.suport.api.domain.Address;
import com.suport.api.domain.Client;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.repository.AddressRepository;
import com.suport.api.utils.AddressModelTests;
import com.suport.api.utils.ClientModelTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AddressControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AddressRepository addressRepository;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
      return "http://localhost:" + port + "/addresses";
    }

    private  Address createAddressInDatabase(){
    return  addressRepository.save(AddressModelTests.createAddressValid());
    
    }
    private <T> HttpEntity<T> jsonEntity(T body) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      return new HttpEntity<>(body, headers);
    }

    @Test
    @DisplayName("FindAll: should return list of address")
    void findAll_ReturnListOfAddress_when_successful() {
        Address savedAddress = createAddressInDatabase();

        ResponseEntity<List<AddressResponseDTO>> response = testRestTemplate.exchange(
                getBaseUrl(),
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
    @DisplayName("FindById: should return address when id exists")
    void findById_ReturnAnAddress_when_successful() {
        Address savedAddress = createAddressInDatabase();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                getBaseUrl()+"/"+savedAddress.getId(),
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
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnThrowBadRequestException_when_idNotExists() {

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                getBaseUrl()+"/999999999",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should return created address")
    void save_ReturnAddress_when_successful() {
        AddressRequestDTO requestDTO = AddressModelTests.createAddressResquestDTOValid();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                getBaseUrl(),
                HttpMethod.POST,
                jsonEntity(requestDTO),
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AddressResponseDTO savedAddress = response.getBody();
        Assertions.assertThat(savedAddress).isNotNull();
        Assertions.assertThat(savedAddress.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: should return updated Address")
    void update_ReturnAddress_when_successful() {
        Address existingAddress = createAddressInDatabase();
        existingAddress.setStreet("alpheneiros");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                getBaseUrl()+"/"+existingAddress.getId(),
                HttpMethod.PUT,
                jsonEntity(existingAddress),
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AddressResponseDTO updatedAddress = response.getBody();
        Assertions.assertThat(updatedAddress).isNotNull();
        Assertions.assertThat(updatedAddress.id()).isNotNull();
        Assertions.assertThat(updatedAddress.street()).isEqualTo("alpheneiros");
    }

    @Test
    @DisplayName("Delete: should delete Address by id")
    void delete_DeleteAddressAndReturnNoContent_when_successful() {
        Address savedAddress = createAddressInDatabase();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                getBaseUrl()+"/"+savedAddress.getId(),
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
