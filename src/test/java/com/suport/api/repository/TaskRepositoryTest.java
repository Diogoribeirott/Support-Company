package com.suport.api.repository;

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
    private TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Test
    @DisplayName("Save: creates task when successful")
    void save_createsTask_whenSuccessful() {
        Task taskValid = TaskModelTests.createtaskValid();
        Task task = taskRepository.save(taskValid);

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isNotNull();
        Assertions.assertThat(task.getClient()).isNotNull();
        Assertions.assertThat(task.getTechnicians()).isNotNull();
        Assertions.assertThat(task.getTitle()).isEqualTo(taskValid.getTitle());
        Assertions.assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Update: updates task when successful")
    void update_updatesTask_whenSuccessful() {
        Technician technician = technicianRepository.save(TechnicianModelTest.updateTechnicianValid());
        Client client = clientRepository.save(ClientModelTest.updateClientValidWithAddress());

        Task task = taskRepository.save(TaskModelTests.createtaskValid());

        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTitle("I don't know");
        task.setClient(client);
        task.setTechnicians(new HashSet<>(Set.of(technician)));

        Task taskUpdated = taskRepository.save(task);

        Assertions.assertThat(taskUpdated).isNotNull();
        Assertions.assertThat(taskUpdated.getId()).isEqualTo(task.getId());
        Assertions.assertThat(taskUpdated.getTitle()).isEqualTo("I don't know");
        Assertions.assertThat(taskUpdated.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        Assertions.assertThat(taskUpdated.getTechnicians()).contains(technician);
        Assertions.assertThat(taskUpdated.getClient()).isEqualTo(client);
    }

    @Test
    @DisplayName("FindById: returns task when ID exists")
    void findById_returnsTask_whenIdExists() {
        Task task = taskRepository.save(TaskModelTests.createtaskValid());

        Optional<Task> taskOptional = taskRepository.findById(task.getId());

        Assertions.assertThat(taskOptional).isPresent().contains(task);
        Assertions.assertThat(taskOptional.get().getId()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("FindById: returns empty when ID does not exist")
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Task> result = taskRepository.findById(9999999L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("FindAll: returns list of tasks when successful")
    void findAll_returnsAllTasks_whenSuccessful() {
        Client client = clientRepository.save(ClientModelTest.createClientValidWithAddress());
        Technician technician = technicianRepository.save(TechnicianModelTest.createtechnicianValid());

        Task task = Task.builder()
                .title("Login system error")
                .description("The Client reported that they are unable to access their account.")
                .status(TaskStatus.OPEN)
                .priority(TaskPriority.HIGH)
                .client(client)
                .technicians(new HashSet<>(Set.of(technician)))
                .build();

        taskRepository.save(task);

        List<Task> taskList = taskRepository.findAll();

        Assertions.assertThat(taskList).hasSize(1);
        Assertions.assertThat(taskList).isNotEmpty();
        Assertions.assertThat(taskList.getFirst().getId()).isEqualTo(task.getId());
        Assertions.assertThat(taskList.getFirst().getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("Delete: deletes task when successful")
    void delete_deletesTask_whenSuccessful() {
        Task task = taskRepository.save(TaskModelTests.createtaskValid());

        taskRepository.deleteById(task.getId());

        Optional<Task> taskOptional = taskRepository.findById(task.getId());
        Assertions.assertThat(taskOptional).isEmpty();
    }

    @Test
    @DisplayName("Delete: does nothing when ID does not exist")
    void delete_doesNothing_whenIdDoesNotExist() {
        Assertions.assertThatCode(() -> taskRepository.deleteById(1L))
                .doesNotThrowAnyException();
    }
}
