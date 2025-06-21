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
import org.springframework.http.*;

import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.repository.TechnicianRepository;
import com.suport.api.utils.TechnicianModelTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TechnicianControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TechnicianRepository technicianRepository;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/technicians";
    }

    @Test
    @DisplayName("FindAll: should return list of technicians")
    void findAll_ReturnsListOfTechnicians_WhenSuccessful() {
        Technician savedTechnician = saveValidTechnician();

        ResponseEntity<List<TechnicianResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});

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

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).satisfies(technician -> {
            Assertions.assertThat(technician.id()).isEqualTo(savedTechnician.getId());
            Assertions.assertThat(technician.name()).isEqualTo(savedTechnician.getName());
        });
    }

    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnsBadRequest_WhenIdDoesNotExist() {
        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + 999999999L,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should return created technician")
    void save_ReturnsCreatedTechnician_WhenSuccessful() {
        Technician technicianRequest = TechnicianModelTest.technicianResquestDTO();

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(technicianRequest),
            new ParameterizedTypeReference<>() {});

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

        ResponseEntity<TechnicianResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.PUT,
            jsonEntity(updateDTO),
            new ParameterizedTypeReference<>() {});

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

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTechnician.getId(),
            HttpMethod.DELETE,
            null,
            Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private Technician saveValidTechnician() {
        return technicianRepository.save(TechnicianModelTest.technicianResquestDTO());
    }

    private <T> HttpEntity<T> jsonEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
