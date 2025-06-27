package com.suport.api.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.context.annotation.Bean;

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.suport.api.domain.Address;
import com.suport.api.domain.UserModel;

import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.request.AuthenticationDTO;

import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.dtos.response.LoginResponseDTO;

import com.suport.api.enums.UserRole;

import com.suport.api.repository.AddressRepository;
import com.suport.api.repository.UserModelRepository;

import com.suport.api.utils.AddressModelTests;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AddressControllerIT {

    // ----------------------
    // Dependencies
    // ----------------------
    @Autowired private TestRestTemplate testRestTemplate;
    @Autowired private AddressRepository addressRepository;
    @Autowired private UserModelRepository userModelRepository;

    @LocalServerPort
    private int port;

    // ----------------------
    // Setup Methods
    // ----------------------

    @BeforeEach
    void setUp() {
        userModelRepository.deleteAll();
        addressRepository.deleteAll();

        UserModel userAdmin = UserModel.builder()
            .login("testAdmin")
            .password(new BCryptPasswordEncoder().encode("testPass123"))
            .role(UserRole.ADMIN) 
            .build();

        UserModel userDefault = UserModel.builder()
            .login("testUser")
            .password(new BCryptPasswordEncoder().encode("testPass123"))
            .role(UserRole.ADMIN) 
            .build();

        userModelRepository.saveAll(List.of(userAdmin, userDefault));
    }

    // ----------------------
    // Helper Methods
    // ----------------------

    private String getBaseUrl() {
        return "http://localhost:" + port + "/addresses";
    }

    private Address createAddressInDatabase() {
        return addressRepository.save(AddressModelTests.createAddressValid());
    }

    private <T> HttpEntity<T> jsonEntity(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private String authenticateAndGetToken(String login, String password) {
        AuthenticationDTO authDTO = new AuthenticationDTO(login, password);

        ResponseEntity<LoginResponseDTO> response = testRestTemplate.postForEntity(
            "http://localhost:" + port + "/auth/login",
            authDTO,
            LoginResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().token();
    }

    // ----------------------
    // Test Cases
    // ----------------------

    @Test
    @DisplayName("FindAll: should return list of address")
    void findAll_ReturnListOfAddress_when_successful() {
        Address savedAddress = createAddressInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<List<AddressResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            jsonEntity(null, token),
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
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedAddress.getId(),
            HttpMethod.GET,
            jsonEntity(null, token),
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
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/999999999",
            HttpMethod.GET,
            jsonEntity(null, token),
            AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should return created address")
    void save_ReturnAddress_when_successful() {
        AddressRequestDTO requestDTO = AddressModelTests.createAddressResquestDTOValid();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(requestDTO, token),
            AddressResponseDTO.class
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
        String token = authenticateAndGetToken("testAdmin", "testPass123");
        existingAddress.setStreet("alpheneiros");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + existingAddress.getId(),
            HttpMethod.PUT,
            jsonEntity(existingAddress, token),
            AddressResponseDTO.class
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
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedAddress.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        boolean exists = addressRepository.existsById(savedAddress.getId());

        Assertions.assertThat(exists).isFalse();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // ----------------------
    // Unauthorized Access Tests
    // ----------------------

    @Test
    @DisplayName("FindAll: should return 401 when token is missing, invalid, or expired")
    void findAllAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        createAddressInDatabase();

        String token = "invalid_token";

        ResponseEntity<String> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            jsonEntity(null, token),
            String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(addressRepository.findAll()).isNotEmpty();
    }

    @Test
    @DisplayName("FindById /address: should return 401 when token is missing, invalid, or expired")
    void findByIdAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        Address savedAddress = createAddressInDatabase();
        String token = "invalid_token";

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedAddress.getId(),
            HttpMethod.GET,
            jsonEntity(null, token),
            AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(addressRepository.existsById(savedAddress.getId())).isTrue();
    }

    @Test
    @DisplayName("FindById /address: Address not exists should return 401 when token is missing, invalid, or expired")
    void findByIdAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalidAndAddressNotExits() {
        String token = "invalid_token";

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/999999999",
            HttpMethod.GET,
            jsonEntity(null, token),
            AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Save /addresses: should return 401 when token is missing, invalid, or expired")
    void saveAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        AddressRequestDTO requestDTO = AddressModelTests.createAddressResquestDTOValid();
        String token = "invalid_token";

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(requestDTO, token),
            AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(addressRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Update /addresses : Should return 401 when token is missing, invalid, or expired")
    void updateAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        Address existingAddress = createAddressInDatabase();
        String token = "invalid_token";
        existingAddress.setStreet("alpheneiros");

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + existingAddress.getId(),
            HttpMethod.PUT,
            jsonEntity(existingAddress, token),
            AddressResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        boolean exists = addressRepository.existsById(existingAddress.getId());
        Assertions.assertThat(exists).isTrue();

        Address address = addressRepository.findById(existingAddress.getId()).get();
        Assertions.assertThat(address.getStreet()).isNotEqualTo("alpheneiros");
    }

    @Test
    @DisplayName("Delete /addresses: should return 401 when token is missing, invalid, or expired")
    void deleteAddress_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        Address savedAddress = createAddressInDatabase();
        String token = "invalid_token";

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedAddress.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        boolean exists = addressRepository.existsById(savedAddress.getId());
        Assertions.assertThat(exists).isTrue();
    }
}
