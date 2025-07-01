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

import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.service.TechnicianService;
import com.suport.api.utils.TechnicianModelTest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TechnicianControllerTest {
   
    @InjectMocks
    private TechnicianController technicianController;

    @Mock
    private TechnicianService technicianServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(technicianServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
                .thenReturn(TechnicianModelTest.technicianValid());

        BDDMockito.when(technicianServiceMock.findAll())
                .thenReturn(List.of(TechnicianModelTest.technicianResponseDTO()));

        BDDMockito.when(technicianServiceMock.save(ArgumentMatchers.any(TechnicianRequestDTO.class)))
                .thenReturn(TechnicianModelTest.technicianResponseDTO());

        BDDMockito.when(technicianServiceMock.update(
                ArgumentMatchers.any(TechnicianRequestDTO.class),
                ArgumentMatchers.anyLong()))
                .thenReturn(TechnicianModelTest.technicianResponseDTO());

        BDDMockito.doNothing().when(technicianServiceMock).delete(ArgumentMatchers.any(Long.class));
    }

  
    // ----------------------------------------
    // DELETE
    // ----------------------------------------

    @Test
    @DisplayName("Delete by id: delete technician by id when successful")
    void delete_deleteTechnicianAndReturnNoContent_whenSuccessful() {
        ResponseEntity<Void> response = technicianController.delete(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

     // ----------------------------------------
    // FIND BY ID
    // ----------------------------------------

    @Test
    @DisplayName("Find by id: find task by id when successful")
    void findById_ReturnTask_whenSuccessful() {
        ResponseEntity<TechnicianResponseDTO> response = technicianController.findById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TechnicianResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isEqualTo(1L);
        Assertions.assertThat(body.name()).isEqualTo("Draven");
        Assertions.assertThat(body.phone()).isEqualTo("(00) 0000-0000");

    }

    @Test
    @DisplayName("Find by id: throw BadRequestException when ID not found")
    void findById_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(technicianServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
                .thenThrow(new BadRequestException("technician not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> technicianController.findById(5L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("technician not found");
    }

    // ----------------------------------------
    // FIND ALL
    // ----------------------------------------

    @Test
    @DisplayName("Find all: return list of technicians")
    void findAll_ReturnListOfTechnicians_whenSuccessful() {
        ResponseEntity<List<TechnicianResponseDTO>> response = technicianController.findAll();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<TechnicianResponseDTO> body = response.getBody();

        Assertions.assertThat(body).isNotEmpty().isNotNull();
        Assertions.assertThat(body).anyMatch(dto ->
                dto.name().equals("Draven") &&
                dto.phone().equals("(00) 0000-0000")
        );
    }


     // ----------------------------------------
    // SAVE
    // ----------------------------------------

    @Test
    @DisplayName("Save: save technician and return technicianResponseDTO")
    void save_ReturnTechnician_whenSuccessful() {
        ResponseEntity<TechnicianResponseDTO> response = technicianController.save(TechnicianModelTest.technicianResquestDTO());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        TechnicianResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isEqualTo(1L);
        Assertions.assertThat(body.name()).isEqualTo("Draven");
        Assertions.assertThat(body.phone()).isEqualTo("(00) 0000-0000");
    }

     // ----------------------------------------
    // UPDATE
    // ----------------------------------------

    @Test
    @DisplayName("Update: update technician and return technicianResponseDTO")
    void update_ReturnTechnician_whenSuccessful() {
        ResponseEntity<TechnicianResponseDTO> response = technicianController.update(TechnicianModelTest.technicianResquestDTO(), 5L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TechnicianResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: throw BadRequestException when ID not found")
    void update_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(technicianServiceMock.update(
                ArgumentMatchers.any(TechnicianRequestDTO.class),
                ArgumentMatchers.any(Long.class)))
                .thenThrow(new BadRequestException("tachnician not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> technicianController.update(TechnicianModelTest.technicianResquestDTO(), 9999L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("tachnician not found");
    }

}
