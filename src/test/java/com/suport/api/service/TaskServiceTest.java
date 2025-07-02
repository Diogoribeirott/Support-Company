package com.suport.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.suport.api.domain.Task;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.repository.TaskRepository;
import com.suport.api.utils.ClientModelTest;
import com.suport.api.utils.TaskModelTests;
import com.suport.api.utils.TechnicianModelTest;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepositoryMock;

    @Mock
    private ClientService clientService;

    @Mock
    private TechnicianService technicianService;


    @BeforeEach
    void setUp(){
        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(TaskModelTests.taskValid()));

        BDDMockito.when(taskRepositoryMock.findAll())
        .thenReturn(List.of(TaskModelTests.taskValid()));

        BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
        .thenReturn(TaskModelTests.taskValid());

         BDDMockito.when(clientService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(ClientModelTest.clientValid());

        BDDMockito.when(technicianService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(TechnicianModelTest.technicianValid());
        
        }

    @Test
    @DisplayName("Find by id: find task by id when successful ")
    void findById_ReturnAnTask_when_sucessful(){
        Task taskValid = TaskModelTests.taskValid();
        Task task = taskService.findByIdOrThrowBadRequestException(1l);

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isEqualTo(taskValid.getId());
        Assertions.assertThat(task.getTitle()).isEqualTo(taskValid.getTitle());
        Assertions.assertThat(task.getClient()).isEqualTo(taskValid.getClient());
        Assertions.assertThat(task.getDescription()).isEqualTo(taskValid.getDescription());

    }

    @Test
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.any()))
            .thenThrow(new BadRequestException("task not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->taskService.findByIdOrThrowBadRequestException(5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("task not found");

    }

    @Test
    @DisplayName("findALL: return list of task")
    void findAll_ReturnListOfTasks_when_sucessful() {
            Task taskValid = TaskModelTests.taskValid();
         List<TaskResponseDTO> listTasks = taskService.findAll();

        Assertions.assertThat(listTasks).isNotEmpty();
        Assertions.assertThat(listTasks).anyMatch(dto ->
                dto.id().equals(taskValid.getId()) &&
                dto.description().equals(taskValid.getDescription()) &&
                dto.title().equals(taskValid.getTitle())
        );
    }

    @Test
    @DisplayName("Save: save taskRequestDTO and return an taskResponse")
    void save_ReturnAnTask_whenSucessful(){

        Task taskValid = TaskModelTests.taskValid();
        TaskResponseDTO taskResponse = taskService.save(TaskModelTests.taskRequestDTO(1l, 2l));

         Assertions.assertThat(taskResponse).isNotNull();
        Assertions.assertThat(taskResponse.id()).isEqualTo(taskValid.getId());
        Assertions.assertThat(taskResponse.title()).isEqualTo(taskValid.getTitle());
        Assertions.assertThat(taskResponse.description()).isEqualTo(taskValid.getDescription());
    }

     @Test
    @DisplayName("Update: update task with taskDTO and long id, return an TaskResponse")
    void update_returnTaskResponse_when_sucessfull() {

          TaskResponseDTO taskResponse = taskService.update(TaskModelTests.taskRequestDTO(1l, 1l), 5l);

        Assertions.assertThat(taskResponse).isNotNull();
        Assertions.assertThat(taskResponse.id()).isNotNull();

    }
    
    @Test
    @DisplayName("Delete by id: delete task by id when successful ")
    void delete_deleteClientAndReturnNoContent_when_Sucessful() {

        Assertions.assertThatCode(() -> taskService.delete(1l)).doesNotThrowAnyException();
    }


}
