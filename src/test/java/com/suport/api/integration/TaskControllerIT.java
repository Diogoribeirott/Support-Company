package com.suport.api.integration;

import java.util.List;
import java.util.Set;

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

import com.suport.api.domain.*;
import com.suport.api.dtos.request.*;
import com.suport.api.dtos.response.*;
import com.suport.api.enums.UserRole;
import com.suport.api.repository.*;
import com.suport.api.utils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TaskControllerIT {

    // ----------------------
    // Dependencies
    // ----------------------
    @Autowired private TestRestTemplate testRestTemplate;
    @Autowired private TaskRepository taskRepository;
    @Autowired private ClientRepository clientRepository;
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
        return "http://localhost:" + port + "/tasks";
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

    private Task createTaskWithClientAndTechnicianInDatabase() {
        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());

        Task task = TaskModelTests.createtaskValid();
        task.setClient(client);
        task.setTechnicians(Set.of(technician));

        return taskRepository.save(task);
    }

     // ----------------------
    // Test Cases
    // ----------------------

    @Test
    @DisplayName("FindAll: should return list of tasks")
    void findAll_ReturnListOfTasks_when_successful() {
        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<List<TaskResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskResponseDTO> tasks = response.getBody();

        Assertions.assertThat(tasks)
            .isNotNull()
            .isNotEmpty()
            .anyMatch(dto -> dto.id().equals(savedTask.getId()));
    }

    @Test
    @DisplayName("FindById: should return task when id exists")
    void findById_ReturnTask_when_successful() {
        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponseDTO task = response.getBody();

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.id()).isEqualTo(savedTask.getId());
        Assertions.assertThat(task.title()).isEqualTo(savedTask.getTitle());
    }

    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnBadRequest_when_idNotExists() {
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<Object> response = testRestTemplate.exchange(
            getBaseUrl() + "/999999999",
            HttpMethod.GET,
            jsonEntity(null, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Save: should create and return task")
    void save_ReturnTask_when_successful() {
        String token = authenticateAndGetToken("testAdmin", "testPass123");
        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());

        TaskRequestCreateDTO taskDTO = TaskModelTests.taskRequestDTO(client.getId(), technician.getId());

        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(taskDTO, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskResponseDTO task = response.getBody();

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.id()).isNotNull();
        Assertions.assertThat(task.description()).isEqualTo(taskDTO.description());
    }

    @Test
    @DisplayName("Update: should return updated task")
    void update_ReturnTask_when_successful() {
        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        Long clientId = savedTask.getClient().getId();
        Long technicianId = savedTask.getTechnicians().stream().findFirst().map(Technician::getId).orElseThrow();

        TaskRequestCreateDTO updateDTO = TaskModelTests.taskRequestDTO(clientId, technicianId);

        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.PUT,
            jsonEntity(updateDTO, token),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponseDTO task = response.getBody();

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.id()).isEqualTo(savedTask.getId());
        Assertions.assertThat(task.description()).isEqualTo(updateDTO.description());
    }

    @Test
    @DisplayName("Delete: should delete task by id")
    void delete_DeleteTaskAndReturnNoContent_when_successful() {
        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        String token = authenticateAndGetToken("testAdmin", "testPass123");

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete /taks: should return 401 when token is missing, invalid, or expired")
    void deleteTask_shouldReturnUnauthorized_whenTokenIsMissingOrInvalid() {
        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        String token = "invalid_token";

        ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.DELETE,
            jsonEntity(null, token),
            Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(taskRepository.existsById(savedTask.getId())).isTrue();
    }

}
