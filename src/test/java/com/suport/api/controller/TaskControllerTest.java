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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.suport.api.dtos.request.TaskRequestCreateDTO;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.service.TaskService;
import com.suport.api.utils.TaskModelTests;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(taskServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
                .thenReturn(TaskModelTests.taskValid());

        BDDMockito.when(taskServiceMock.findAll())
                .thenReturn(List.of(TaskModelTests.taskResponseDTO()));

        BDDMockito.when(taskServiceMock.save(ArgumentMatchers.any(TaskRequestCreateDTO.class)))
                .thenReturn(TaskModelTests.taskResponseDTO());

        BDDMockito.when(taskServiceMock.update(
                ArgumentMatchers.any(TaskRequestCreateDTO.class),
                ArgumentMatchers.anyLong()))
                .thenReturn(TaskModelTests.taskResponseDTO());

        BDDMockito.doNothing().when(taskServiceMock).delete(ArgumentMatchers.any(Long.class));
    }

    // ----------------------------------------
    // DELETE
    // ----------------------------------------

    @Test
    @DisplayName("Delete by id: delete task by id when successful")
    void delete_deleteTaskAndReturnNoContent_whenSuccessful() {
        ResponseEntity<Void> response = taskController.delete(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // ----------------------------------------
    // FIND BY ID
    // ----------------------------------------

    @Test
    @DisplayName("Find by id: find task by id when successful")
    void findById_ReturnTask_whenSuccessful() {
        ResponseEntity<TaskResponseDTO> response = taskController.findById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isEqualTo(1L);
        Assertions.assertThat(body.title()).isEqualTo("Login system error");
        Assertions.assertThat(body.description()).isEqualTo("The Client reported that they are unable to access their account.");
        Assertions.assertThat(body.status()).isEqualTo(TaskStatus.CLOSED);
        Assertions.assertThat(body.priority()).isEqualTo(TaskPriority.HIGH);
        Assertions.assertThat(body.clientId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Find by id: throw BadRequestException when ID not found")
    void findById_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(taskServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
                .thenThrow(new BadRequestException("task not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskController.findById(5L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("task not found");
    }

    // ----------------------------------------
    // FIND ALL
    // ----------------------------------------

    @Test
    @DisplayName("Find all: return list of tasks")
    void findAll_ReturnListOfTasks_whenSuccessful() {
        ResponseEntity<List<TaskResponseDTO>> response = taskController.findAll();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<TaskResponseDTO> body = response.getBody();

        Assertions.assertThat(body).isNotEmpty().isNotNull();
        Assertions.assertThat(body).anyMatch(dto ->
                dto.title().equals("Login system error") &&
                dto.description().equals("The Client reported that they are unable to access their account.") &&
                dto.status().equals(TaskStatus.CLOSED)
        );
    }

    // ----------------------------------------
    // SAVE
    // ----------------------------------------

    @Test
    @DisplayName("Save: save task and return TaskResponseDTO")
    void save_ReturnTask_whenSuccessful() {
        ResponseEntity<TaskResponseDTO> response = taskController.save(TaskModelTests.taskRequestDTO(1L, 1L));

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        TaskResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isEqualTo(1L);
        Assertions.assertThat(body.title()).isEqualTo("Login system error");
        Assertions.assertThat(body.description()).isEqualTo("The Client reported that they are unable to access their account.");
        Assertions.assertThat(body.status()).isEqualTo(TaskStatus.CLOSED);
        Assertions.assertThat(body.priority()).isEqualTo(TaskPriority.HIGH);
        Assertions.assertThat(body.clientId()).isEqualTo(1L);
    }

    // ----------------------------------------
    // UPDATE
    // ----------------------------------------

    @Test
    @DisplayName("Update: update task and return TaskResponseDTO")
    void update_ReturnTask_whenSuccessful() {
        ResponseEntity<TaskResponseDTO> response = taskController.update(TaskModelTests.taskRequestDTO(1L, 1L), 5L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: throw BadRequestException when ID not found")
    void update_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(taskServiceMock.update(
                ArgumentMatchers.any(TaskRequestCreateDTO.class),
                ArgumentMatchers.any(Long.class)))
                .thenThrow(new BadRequestException("task not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskController.update(TaskModelTests.taskRequestDTO(999L, 9999L), 9999L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("task not found");
    }
}
