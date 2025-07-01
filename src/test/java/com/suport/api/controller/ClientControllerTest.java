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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.request.ClientRequestUpdateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.enums.ClientType;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.service.ClientService;
import com.suport.api.utils.ClientModelTest;

@ExtendWith(SpringExtension.class)
public class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(clientServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(Long.class)))
                .thenReturn(ClientModelTest.clientValid());

        BDDMockito.when(clientServiceMock.findAll())
                .thenReturn(List.of(ClientModelTest.clientResponseDTO()));

        BDDMockito.when(clientServiceMock.save(ArgumentMatchers.any(ClientRequestCreateDTO.class)))
                .thenReturn(ClientModelTest.clientResponseDTO());

        BDDMockito.when(clientServiceMock.update(
                ArgumentMatchers.any(ClientRequestUpdateDTO.class),
                ArgumentMatchers.anyLong()))
                .thenReturn(ClientModelTest.clientResponseDTO());

        BDDMockito.doNothing().when(clientServiceMock).deleteById(ArgumentMatchers.any(Long.class));
    }

    @Test
    void test() {
        // Placeholder para futuros testes
    }

    @Test
    @DisplayName("Delete by id: delete client by id when successful")
    void delete_deleteClientAndReturnNoContent_whenSuccessful() {
        ResponseEntity<Void> response = clientController.deleteById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Find by id: find client by id when successful")
    void findById_ReturnClient_whenSuccessful() {
        ResponseEntity<ClientResponseDTO> response = clientController.findById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ClientResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isEqualTo(1L);
        Assertions.assertThat(body.name()).isEqualTo("Draven");
        Assertions.assertThat(body.email()).isEqualTo("Draven@gmail.com");
        Assertions.assertThat(body.taxId()).isEqualTo("123.456.789-00");
        Assertions.assertThat(body.phone()).isEqualTo("00 0000-0000");
    }

    @Test
    @DisplayName("Find by id: throw BadRequestException when ID not found")
    void findById_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(clientServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
                .thenThrow(new BadRequestException("Address not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> clientController.findById(5L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");
    }

    @Test
    @DisplayName("Find all: return list of clients")
    void findAll_ReturnListOfClients_whenSuccessful() {
        ResponseEntity<List<ClientResponseDTO>> response = clientController.findAll();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ClientResponseDTO> body = response.getBody();

        Assertions.assertThat(body).isNotEmpty().isNotNull();
        Assertions.assertThat(body).anyMatch(dto ->
                dto.name().equals("Draven") &&
                dto.phone().equals("00 0000-0000") &&
                dto.email().equals("Draven@gmail.com")
        );
    }

    @Test
    @DisplayName("Save: save client and return ClientResponseDTO")
    void save_ReturnClient_whenSuccessful() {
        ResponseEntity<ClientResponseDTO> response = clientController.save(ClientModelTest.clientRequestCreateDTO());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ClientResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
        Assertions.assertThat(body.name()).isEqualTo("Draven");
        Assertions.assertThat(body.email()).isEqualTo("Draven@gmail.com");
        Assertions.assertThat(body.taxId()).isEqualTo("123.456.789-00");
        Assertions.assertThat(body.phone()).isEqualTo("00 0000-0000");
        Assertions.assertThat(body.type()).isEqualTo(ClientType.INDIVIDUAL);
    }

    @Test
    @DisplayName("Update: update client and return ClientResponseDTO")
    void update_ReturnClient_whenSuccessful() {
        ResponseEntity<ClientResponseDTO> response = clientController.update(ClientModelTest.clientRequestUpdateDTO(), 5L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ClientResponseDTO body = response.getBody();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.id()).isNotNull();
    }

    @Test
    @DisplayName("Update: throw BadRequestException when ID not found")
    void update_ThrowsBadRequestException_whenIdNotExists() {
        BDDMockito.when(clientServiceMock.update(
                ArgumentMatchers.any(ClientRequestUpdateDTO.class),
                ArgumentMatchers.any(Long.class)))
                .thenThrow(new BadRequestException("Address not found"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> clientController.update(ClientModelTest.clientRequestUpdateDTO(), 9999L)
        );

        Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");
    }
}
