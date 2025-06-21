package com.suport.api.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Technician;
import com.suport.api.utils.TechnicianModelTest;

@DataJpaTest
@DisplayName("Tests for the Technician repository")
public class TechnicianRepositoryTest {

    @Autowired
    private TechnicianRepository technicianRepository;

    private Technician technicianValid;

    @BeforeEach
    void setUp(){
        technicianValid = TechnicianModelTest.createtechnicianValid();
    }

    @Test
    @DisplayName("Save: creates technician when successful")
    void save_createsTechnician_whenSuccessful() {
        Technician technician = technicianRepository.save(technicianValid);

        Assertions.assertThat(technician.getId()).isNotNull();
        Assertions.assertThat(technician)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(technician);
    }

    @Test
    @DisplayName("Update: updates technician when successful")
    void update_updatesTechnician_whenSuccessful() {
        Technician technician = technicianRepository.save(technicianValid);

        technician.setName("Draven");
        technician.setPhone("(77) 0000-0000");

        Technician technicianUpdated = technicianRepository.save(technician);

        Assertions.assertThat(technicianUpdated).isNotNull();
        Assertions.assertThat(technicianUpdated.getId()).isNotNull();
        Assertions.assertThat(technicianUpdated.getName()).isEqualTo("Draven");
        Assertions.assertThat(technicianUpdated.getPhone()).isEqualTo("(77) 0000-0000");
    }

    @Test
    @DisplayName("FindById: returns technician when ID exists")
    void findById_returnsTechnician_whenIdExists() {
        Technician technician = technicianRepository.save(technicianValid);

        Optional<Technician> technicianOptional = technicianRepository.findById(technician.getId());

        Assertions.assertThat(technicianOptional).isPresent().contains(technician);

        Technician foundTechnician = technicianOptional.get();

        Assertions.assertThat(foundTechnician).isNotNull();
        Assertions.assertThat(foundTechnician)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(technician);
    }

    @Test
    @DisplayName("FindById: returns empty when ID does not exist")
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Technician> result = technicianRepository.findById(9999999L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("FindAll: returns a list of technicians when successful")
    void findAll_returnsAllTechnicians_whenSuccessful() {
        Technician technician = technicianRepository.save(technicianValid);

        List<Technician> technicianList = technicianRepository.findAll();

        Assertions.assertThat(technicianList).isNotEmpty().isNotNull();
        Assertions.assertThat(technicianList)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
        .contains(technician);
    }

    @Test
    @DisplayName("Delete: deletes technician when successful")
    void delete_deletesTechnician_whenSuccessful() {
        Technician technician = technicianRepository.save(technicianValid);

        technicianRepository.deleteById(technician.getId());
        Optional<Technician> technicianOptional = technicianRepository.findById(technician.getId());

        Assertions.assertThat(technicianOptional).isEmpty();
    }

    @Test
    @DisplayName("Delete: does nothing when ID does not exist")
    void delete_doesNothing_whenIdDoesNotExist() {
        Assertions.assertThatCode(() -> technicianRepository.deleteById(1L))
                  .doesNotThrowAnyException();
    }
}
