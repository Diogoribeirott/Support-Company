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

import com.suport.api.domain.Technician;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.repository.TechnicianRepository;
import com.suport.api.utils.TechnicianModelTest;

@ExtendWith(SpringExtension.class)
public class TechnicianTest {

    @InjectMocks
    private TechnicianService technicianService;

    @Mock
    private TechnicianRepository technicianRepositoryMock;

    @BeforeEach
    void setUp(){
        BDDMockito.when(technicianRepositoryMock.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(TechnicianModelTest.technicianValid()));

        BDDMockito.when(technicianRepositoryMock.findAll())
        .thenReturn(List.of(TechnicianModelTest.technicianValid()));

        BDDMockito.when(technicianRepositoryMock.save(ArgumentMatchers.any(Technician.class)))
        .thenReturn(TechnicianModelTest.technicianValid());

        BDDMockito.doNothing().when(technicianRepositoryMock).delete(ArgumentMatchers.any(Technician.class));

    }


    @Test
    @DisplayName("Find by id: find technician by id when successful ")
    void findById_ReturnAnTechnician_when_sucessful(){
        Technician technicianValid = TechnicianModelTest.technicianValid();
        Technician technician = technicianService.findByIdOrThrowBadRequestException(1l);

        Assertions.assertThat(technician).isNotNull();
        Assertions.assertThat(technician.getId()).isNotNull();
        Assertions.assertThat(technician.getId()).isEqualTo(technicianValid.getId());
        Assertions.assertThat(technician.getName()).isEqualTo(technicianValid.getName());
        Assertions.assertThat(technician.getPhone()).isEqualTo(technicianValid.getPhone());

    }

    @Test
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        BDDMockito.when(technicianRepositoryMock.findById(ArgumentMatchers.any()))
            .thenThrow(new BadRequestException("technician not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->technicianService.findByIdOrThrowBadRequestException(5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("technician not found");

    }

     @Test
    @DisplayName("FindAll: return an list of technician  when successful ")
    void findAll_ReturnListTechnician_when_sucessful(){
        Technician technicianValid = TechnicianModelTest.technicianValid();
        List<TechnicianResponseDTO> listTechnician = technicianService.findAll();

        Assertions.assertThat(listTechnician).isNotEmpty();
        Assertions.assertThat(listTechnician).anyMatch(dto ->
                dto.id().equals(technicianValid.getId()) &&
                dto.name().equals(technicianValid.getName()) &&
                dto.phone().equals(technicianValid.getPhone())
        );

    }

      @Test
    @DisplayName("save: save TechnicianRequestDTO and return an TechnicianResponse ")
    void  save_ReturnAnTechnician_whenSucessful(){
        Technician technicianValid = TechnicianModelTest.technicianValid();
      TechnicianResponseDTO technicianResponse = technicianService.save(TechnicianModelTest.technicianResquestDTO());

         Assertions.assertThat(technicianResponse).isNotNull();
        Assertions.assertThat(technicianResponse.id()).isNotNull();
        Assertions.assertThat(technicianResponse.id()).isEqualTo(technicianValid.getId());
        Assertions.assertThat(technicianResponse.name()).isEqualTo(technicianValid.getName());
        Assertions.assertThat(technicianResponse.phone()).isEqualTo(technicianValid.getPhone());

    }

     @Test
    @DisplayName("Update: update technician with TechnicianDTO and long id, return an TechnicianResponse ")
    void  update_UpdatedAnTechnician_whenSucessful(){
        Technician technicianValid = TechnicianModelTest.technicianValid();
       TechnicianResponseDTO technicianResponse = technicianService.update(TechnicianModelTest.technicianResquestDTO(),1l);

         Assertions.assertThat(technicianResponse).isNotNull();
        Assertions.assertThat(technicianResponse.id()).isNotNull();
        Assertions.assertThat(technicianResponse.id()).isEqualTo(technicianValid.getId());
        Assertions.assertThat(technicianResponse.name()).isEqualTo(technicianValid.getName());
        Assertions.assertThat(technicianResponse.phone()).isEqualTo(technicianValid.getPhone());

    }

      @Test
    @DisplayName("Delete: delete an technician ")
    void  delete_deleteAnTechnician_whenSucessful(){
          Assertions.assertThatCode(() -> technicianService.delete(1l)).doesNotThrowAnyException();
    }

}
