package com.suport.api.repository;

import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
import com.suport.api.enums.TaskPriority;
import com.suport.api.enums.TaskStatus;
import com.suport.api.utils.ClientModelTest;
import com.suport.api.utils.TaskModelTests;
import com.suport.api.utils.TechnicianModelTest;

@DataJpaTest
@DisplayName("Tests for the Task repository")
public class TaskRepositoryTest {


    @Autowired
    private  TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Test
    @DisplayName("Save: creates task when successful")
    void save_createtask_when_successful(){
        Task taskValid = TaskModelTests.createtaskValid();
        Task task = taskRepository.save(taskValid);

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isNotNull();
        Assertions.assertThat(task.getClient()).isNotNull();
        Assertions.assertThat(task.getCreatedAt()).isNotNull();
        Assertions.assertThat(task.getTechnicians()).isNotNull();
        Assertions.assertThat(task.getTitle()).isEqualTo(taskValid.getTitle());

    }

    @Test
    @DisplayName("Update: Update task when successful")
    void Update_Updatetask_when_Successful(){
        Technician technician = TechnicianModelTest.updateTechnicianValid();
        Client client = ClientModelTest.updateClientValidWithAddress();

        Task taskValid = TaskModelTests.createtaskValid();
        Task task = taskRepository.save(taskValid);

        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTitle("I don't know"); 
        task.setClient(client);
        task.setTechnicians( new HashSet<>(Set.of(technician)) );

        Task taskUpdate = taskRepository.save(task);

        Assertions.assertThat(taskUpdate).isNotNull();
        Assertions.assertThat(taskUpdate.getId()).isNotNull();
        Assertions.assertThat(taskUpdate.getStatus()).isEqualTo(task.getStatus());
        Assertions.assertThat(taskUpdate.getTitle()).isEqualTo(task.getTitle());
        Assertions.assertThat(taskUpdate.getTechnicians().contains(technician));
        Assertions.assertThat(taskUpdate.getClient()).isEqualTo(client);

    }

    @Test
    @DisplayName("FindbyId: return optinal with task when successful")
    void findById_findByIdtask_when_Successful(){
        Task taskValid = TaskModelTests.createtaskValid();
        Task task = taskRepository.save(taskValid);

        Optional<Task> taskOptional = taskRepository.findById(task.getId());

        Assertions.assertThat(taskOptional).isNotEmpty();
        Assertions.assertThat(taskOptional).isPresent().contains(task);

         Task foundtask = taskOptional.get();

        Assertions.assertThat(foundtask).isNotNull();
        Assertions.assertThat(foundtask.getId()).isNotNull();

    }

    @Test
    @DisplayName("FindbyId: return optinal empty when successful")
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Task> result = taskRepository.findById(9999999l);

        Assertions.assertThat(result).isEmpty(); 
    }

     @Test
    @DisplayName("FindAll: Returns a list of addresses when successful")
    void findAll_ReturnAllAddress_when_Successful(){
        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());

        Task task = TaskModelTests.createtaskValid();
        task.setClient(client);
        task.setTechnicians(Set.of(technician));

        taskRepository.save(task);

        List<Task> taskList = taskRepository.findAll();

        Assertions.assertThat(taskList).hasSize(1);
        Assertions.assertThat(taskList.isEmpty()).isFalse();
        Assertions.assertThat(taskList.getFirst().getId()).isNotNull();
        Assertions.assertThat(taskList.getFirst().getId()).isEqualTo(task.getId());
        Assertions.assertThat(taskList.getFirst().getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("delete: delete task when successful")
    void delete_deleteAddress_when_Successful(){
        Task taskValid = TaskModelTests.createtaskValid();
        Task task = taskRepository.save(taskValid);

        taskRepository.deleteById(task.getId());
        Optional<Task> taskOptional = taskRepository.findById(task.getId());
    
         Assertions.assertThat(taskOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("delete: does nothing when ID does not exist")
    void delete_doesNothing_when_NotSuccessful(){

        Assertions.assertThatCode(() ->taskRepository.deleteById(1l)).doesNotThrowAnyException();

    }

}
