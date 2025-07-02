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

import com.suport.api.domain.Client;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.repository.ClientRepository;
import com.suport.api.utils.ClientModelTest;

@ExtendWith(SpringExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepositoryMock;

     @BeforeEach
    void setUp(){

        BDDMockito.when(clientRepositoryMock.findById(ArgumentMatchers.any(Long.class)))
            .thenReturn(Optional.of(ClientModelTest.clientValid()));

        BDDMockito.when(clientRepositoryMock.findAll())
            .thenReturn( List.of(ClientModelTest.clientValid()));

        BDDMockito.when(clientRepositoryMock.save(ArgumentMatchers.any(Client.class)))
            .thenReturn( ClientModelTest.clientValid());
        

        BDDMockito.doNothing().when(clientRepositoryMock).delete( ClientModelTest.clientValid());

    }

    @Test
    @DisplayName("Delete by id: delete client by id when successful ")
    void delete_deleteClientAndReturnNoContent_when_Sucessful() {

        Assertions.assertThatCode(() -> clientService.deleteById(1l)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Find by id: find clinet by id when successful ")
    void findById_ReturnAnClient_when_sucessful() {

        Client clientValid = ClientModelTest.clientValid();
       Client client = clientService.findByIdOrThrowBadRequestException(5l);

        Assertions.assertThat(client).isNotNull();
        Assertions.assertThat(client.getId()).isNotNull();
        Assertions.assertThat(client.getId()).isEqualTo(clientValid.getId());
        Assertions.assertThat(client.getName()).isEqualTo(clientValid.getName());
        Assertions.assertThat(client.getEmail()).isEqualTo(clientValid.getEmail());
        Assertions.assertThat(client.getPhone()).isEqualTo(clientValid.getPhone());
        Assertions.assertThat(client.getTaxId()).isEqualTo(clientValid.getTaxId());

    }

     @Test
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        BDDMockito.when(clientRepositoryMock.findById(ArgumentMatchers.any()))
            .thenThrow(new BadRequestException("client not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->clientService.findByIdOrThrowBadRequestException(5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("client not found");

    }

    @Test
    @DisplayName("findALL: return list of clients")
    void findAll_ReturnListOfClients_when_sucessful() {

         Client clientValid = ClientModelTest.clientValid();
        List<ClientResponseDTO> listClients = clientService.findAll();

        Assertions.assertThat(listClients).isNotEmpty();
        Assertions.assertThat(listClients).anyMatch(dto ->
                dto.id().equals(clientValid.getId()) &&
                dto.phone().equals(clientValid.getPhone()) &&
                dto.email().equals(clientValid.getEmail())
        );
    }

     @Test
    @DisplayName("Save: save clientDTO and return an clientResponse")
    void save_returnClientResponse_when_sucessfull() {

        Client clientValid = ClientModelTest.clientValid();
       ClientResponseDTO clientesponse = clientService.save(ClientModelTest.clientRequestCreateDTO());

       Assertions.assertThat(clientesponse).isNotNull();
        Assertions.assertThat(clientesponse.email()).isEqualTo(clientValid.getEmail());
        Assertions.assertThat(clientesponse.name()).isEqualTo(clientValid.getName());
        Assertions.assertThat(clientesponse.phone()).isEqualTo(clientValid.getPhone());
        Assertions.assertThat(clientesponse.taxId()).isEqualTo(clientValid.getTaxId());
      
    }

     @Test
    @DisplayName("Update: update client with clientDTO and long id, return an clientResponse")
    void update_returnClientResponse_when_sucessfull() {

         ClientResponseDTO clientResponse = clientService.update(ClientModelTest.clientRequestUpdateDTO(), 5l);

        Assertions.assertThat(clientResponse).isNotNull();
        Assertions.assertThat(clientResponse.id()).isNotNull();

    }



}
