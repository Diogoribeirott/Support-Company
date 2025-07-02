package com.suport.api.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
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
    
    private Task taskValid;
    private Technician technician;
    private Client client;

    @BeforeEach
    void setUp(){
        client = clientRepository.save(ClientModelTest.clientValid2());
        technician = technicianRepository.save(TechnicianModelTest.technicianValid2());
        taskValid = TaskModelTests.taskValid2(client,technician);
    }

    @Test
    @DisplayName("Save: creates task when successful")
    void save_createtask_when_successful(){
        Task task = taskRepository.save(taskValid);

        Assertions.assertThat(task).isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(taskValid);

    }

    @Test
    @DisplayName("Update: Update task when successful")
    void Update_Updatetask_when_Successful(){
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
        Task task = taskRepository.save(taskValid);

        Optional<Task> taskOptional = taskRepository.findById(task.getId());

        Assertions.assertThat(taskOptional).isPresent();

        Task foundTask = taskOptional.get();

        Assertions.assertThat(foundTask).isNotNull();
        Assertions.assertThat(foundTask)
         .usingRecursiveComparison()
         .ignoringFields("id")
         .isEqualTo(task);
    }

    @Test
    @DisplayName("FindbyId: return optinal empty when id not found")
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Task> result = taskRepository.findById(9999999l);

        Assertions.assertThat(result).isEmpty(); 
    }

     @Test
    @DisplayName("FindAll: Returns a list of tasks when successful")
    void findAll_ReturnAllAddress_when_Successful(){
        Client client = clientRepository.save(ClientModelTest.clientValid2());
        Technician technician = technicianRepository.save(TechnicianModelTest.technicianValid2());

        Task task = TaskModelTests.taskValid2(client,technician);

        taskRepository.save(task);

        List<Task> taskList = taskRepository.findAll();

        Assertions.assertThat(taskList).isNotNull().isNotEmpty();
        Assertions.assertThat(taskList)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
        .contains(task);
    }

    @Test
    @DisplayName("Delete: deletes task when successful")
    void delete_deleteAddress_when_Successful(){
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
