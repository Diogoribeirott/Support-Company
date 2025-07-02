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
import org.springframework.http.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.suport.api.domain.Technician;
import com.suport.api.domain.UserModel;
import com.suport.api.dtos.request.AuthenticationDTO;
import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.LoginResponseDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.enums.UserRole;
import com.suport.api.repository.TechnicianRepository;
import com.suport.api.repository.UserModelRepository;
import com.suport.api.utils.TechnicianModelTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TechnicianControllerIT {

    // ----------------------
    // Dependencies
    // ----------------------
    
    @Autowired private TestRestTemplate testRestTemplate;
    @Autowired private TechnicianRepository technicianRepository;
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
        return "http://localhost:" + port + "/technicians";
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

    private <T> HttpEntity<T> jsonEntity(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private Technician saveValidTechnician() {
        return technicianRepository.save(TechnicianModelTest.updateTechnicianValid());
    }

    // ========== Test Cases ==========

    @Test
    @DisplayName("FindAll: should return list of technicians")
    void findAll_ReturnsListOfTechnicians_WhenSuccessful() {
        Technician savedTechnician = saveValidTechnician();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<List<TechnicianResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody())
            .isNotNull()
            .isNotEmpty()
            .anyMatch(dto -> dto.id().equals(savedTechnician.getId()));
    }

    @Test
    @DisplayName("FindById: should return technician when id exists")
    void findById_ReturnsTechnician_WhenSuccessful() {
        Technician savedTechnician = saveValidTechnician();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.GET,
            jsonEntity(null, token),
            TechnicianResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).satisfies(technician -> {
            Assertions.assertThat(technician.id()).isEqualTo(savedTechnician.getId());
            Assertions.assertThat(technician.name()).isEqualTo(savedTechnician.getName());
        });
    }

    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnsBadRequest_WhenIdDoesNotExist() {
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + 999999999L,
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should return created technician")
    void save_ReturnsCreatedTechnician_WhenSuccessful() {
        Technician technicianRequest = TechnicianModelTest.technicianValid();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(technicianRequest, token),
            TechnicianResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).satisfies(technician -> {
            Assertions.assertThat(technician.id()).isNotNull();
            Assertions.assertThat(technician.name()).isEqualTo(technicianRequest.getName());
        });
    }

    @Test
    @DisplayName("Update: should return updated technician")
    void update_ReturnsUpdatedTechnician_WhenSuccessful() {
        Technician savedTechnician = saveValidTechnician();
        TechnicianRequestDTO updateDTO = new TechnicianRequestDTO("Draven", savedTechnician.getPhone());
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.PUT,
            jsonEntity(updateDTO, token),
            TechnicianResponseDTO.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).satisfies(technician -> {
            Assertions.assertThat(technician.id()).isEqualTo(savedTechnician.getId());
            Assertions.assertThat(technician.name()).isEqualTo("Draven");
        });
    }

    @Test
    @DisplayName("Delete: should delete technician by id")
    void delete_DeletesTechnician_WhenSuccessful() {
        Technician savedTechnician = saveValidTechnician();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete: should return 401 when token is missing, invalid, or expired")
    void deleteTechnicians_ShouldReturnUnauthorized_WhenTokenIsInvalid() {
        Technician savedTechnician = saveValidTechnician();
        String token = "invalid_token";

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(technicianRepository.existsById(savedTechnician.getId())).isTrue();
    }
}
