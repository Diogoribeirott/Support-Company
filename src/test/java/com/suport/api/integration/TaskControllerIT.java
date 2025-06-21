package com.suport.api.integration;

import java.util.List;
import java.util.Set;

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

import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TaskRequestCreateDTO;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.repository.ClientRepository;
import com.suport.api.repository.TaskRepository;
import com.suport.api.repository.TechnicianRepository;
import com.suport.api.utils.ClientModelTest;
import com.suport.api.utils.TaskModelTests;
import com.suport.api.utils.TechnicianModelTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TaskControllerIT {
    
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private TechnicianRepository technicianRepository;
    
    @LocalServerPort
    private int port;
    
    private Task createTaskWithClientAndTechnicianInDatabase(){
        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());
        
        Task taskTest = TaskModelTests.createtaskValid();
        taskTest.setClient(client);
        taskTest.setTechnicians(Set.of(technician));
        
        return taskRepository.save(taskTest);
    }
    
    private <T> HttpEntity<T> jsonEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
    private String getBaseUrl() {
        return "http://localhost:" + port + "/tasks";
    }


    @Test
    @DisplayName("FindAll: should return list of task")
    void findAll_ReturnListOfTasks_when_successful() {

        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        
        ResponseEntity<List<TaskResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskResponseDTO>>() {}
            );
            
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            
            List<TaskResponseDTO> taskList = response.getBody();
            
            Assertions.assertThat(taskList)
            .isNotNull()
            .isNotEmpty()
            .anyMatch(dto -> dto.id().equals(savedTask.getId()));
        }
        
        @Test
        @DisplayName("FindById: should return task when id exists")
        void findById_ReturnTask_when_successful() {

           Task savedTask = createTaskWithClientAndTechnicianInDatabase();
            
            ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
                getBaseUrl() + "/" + savedTask.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TaskResponseDTO>() {}
                );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        
        TaskResponseDTO task = response.getBody();

        Assertions.assertThat(task.id()).isEqualTo(savedTask.getId());
        Assertions.assertThat(task.title()).isEqualTo(savedTask.getTitle());
    }
    
    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnBadRequest_when_idNotExists() {
        ResponseEntity<Object> response = testRestTemplate.exchange(
            getBaseUrl() + "/999999999",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Object>() {}
            );
            
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        
        @Test
        @DisplayName("Save: save TaskRequestCreateDTO and return TaskResponseDTO")
        void save_ReturnTask_when_successful() {

        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());
        
        TaskRequestCreateDTO taskRequestDTO = TaskModelTests.taskRequestDTO(client.getId(), technician.getId());
        
        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(taskRequestDTO),
            new ParameterizedTypeReference<TaskResponseDTO>() {}
            );
            
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            Assertions.assertThat(response.getBody()).isNotNull();
            
        TaskResponseDTO task = response.getBody();
        Assertions.assertThat(task.id()).isNotNull();
        Assertions.assertThat(task.description()).isEqualTo(taskRequestDTO.description());
    }
    
    @Test
    @DisplayName("Update: should return updated task")
    void update_ReturnTask_when_successful() {

        Task savedTask = createTaskWithClientAndTechnicianInDatabase();
        
        Long clientId = savedTask.getClient().getId();
        Long technicianId = savedTask.getTechnicians().stream().findFirst().map(Technician::getId).orElseThrow();

        TaskRequestCreateDTO taskRequestDTO = TaskModelTests.taskRequestDTO(clientId, technicianId);
        
        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.PUT,
            jsonEntity(taskRequestDTO),
            new ParameterizedTypeReference<TaskResponseDTO>() {}
            );
            
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
            
            TaskResponseDTO task = response.getBody();
            Assertions.assertThat(task.id()).isEqualTo(savedTask.getId());
            Assertions.assertThat(task.description()).isEqualTo(taskRequestDTO.description());
        }
        
        @Test
        @DisplayName("Delete: should delete task by id")
        void delete_DeleteTaskAndReturnNoContent_when_successful() {

           Task savedTask = createTaskWithClientAndTechnicianInDatabase();
            
            ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedTask.getId(),
            HttpMethod.DELETE,
            null,
            Void.class
            );
            
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }


    }
    