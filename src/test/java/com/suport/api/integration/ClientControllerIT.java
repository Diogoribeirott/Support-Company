package com.suport.api.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.suport.api.domain.Client;
import com.suport.api.domain.UserModel;

import com.suport.api.dtos.request.AuthenticationDTO;
import com.suport.api.dtos.request.ClientRequestCreateDTO;

import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.dtos.response.LoginResponseDTO;

import com.suport.api.enums.UserRole;

import com.suport.api.repository.ClientRepository;
import com.suport.api.repository.UserModelRepository;

import com.suport.api.utils.ClientModelTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ClientControllerIT {

    // ----------------------
    // Dependencies
    // ----------------------
    @Autowired private TestRestTemplate testRestTemplate;
    @Autowired private ClientRepository clientRepository;
    @Autowired private UserModelRepository userModelRepository;

    @LocalServerPort
    private int port;


    // ----------------------
    // Setup
    // ----------------------

    @BeforeEach
    void setUp() {
        userModelRepository.deleteAll();

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
    // Helpers
    // ----------------------

    private String getBaseUrl() {
        return "http://localhost:" + port + "/clients";
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

    private Client createClientInDatabase() {
        return clientRepository.save(ClientModelTest.clientValid2());
    }

    private <T> HttpEntity<T> jsonEntity(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    // ----------------------
    // Test Cases
    // ----------------------

    @Test
    @DisplayName("FindAll: should return list of client")
    void findAll_ReturnListOfClient_when_successful() {
        Client savedClient = createClientInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<List<ClientResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<List<ClientResponseDTO>>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ClientResponseDTO> clientList = response.getBody();

        Assertions.assertThat(clientList)
            .isNotNull()
            .isNotEmpty()
            .anyMatch(dto -> dto.id().equals(savedClient.getId()));
    }

    @Test
    @DisplayName("FindById: should return client when id exists")
    void findById_ReturnAnClient_when_successful() {
        Client savedClient = createClientInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedClient.getId(),
            HttpMethod.GET,
            jsonEntity(null, token),
            ClientResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isEqualTo(savedClient.getId());
        Assertions.assertThat(client.name()).isEqualTo(savedClient.getName());
        Assertions.assertThat(client.email()).isEqualTo(savedClient.getEmail());
    }

    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnAnThrowBadRequestException_when_idNotExists() {
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + 99999999L,
            HttpMethod.GET,
            jsonEntity(null, token),
            ClientResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should return created client")
    void save_ReturnAddress_when_successful() {
        ClientRequestCreateDTO clientRequestCreateDTO = ClientModelTest.clientRequestCreateDTO2();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(clientRequestCreateDTO, token),
            ClientResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isNotNull();
        Assertions.assertThat(client.name()).isEqualTo(clientRequestCreateDTO.name());
        Assertions.assertThat(client.email()).isEqualTo(clientRequestCreateDTO.email());
    }

    @Test
    @DisplayName("Update: should return updated client")
    void update_ReturnAddress_when_successful() {
        Client savedClient = createClientInDatabase();
        savedClient.setName("Draven");

        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedClient.getId(),
            HttpMethod.PUT,
            jsonEntity(ClientModelTest.clientRequestUpdateDTO(), token),
            ClientResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isNotNull();
        Assertions.assertThat(client.id()).isEqualTo(savedClient.getId());
        Assertions.assertThat(client.name()).isEqualTo(savedClient.getName());
    }

    @Test
    @DisplayName("Delete: should delete client by id")
    void delete_DeleteClientAndReturnNoContent_when_successful() {
        Client savedClient = createClientInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedClient.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete /clients: should return 401 when token is missing, invalid, or expired")
    void deleteClient_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        Client savedClient = createClientInDatabase();
        String token = "invalid_token";

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedClient.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        boolean exists = clientRepository.existsById(savedClient.getId());
        Assertions.assertThat(exists).isTrue();
    }
}
