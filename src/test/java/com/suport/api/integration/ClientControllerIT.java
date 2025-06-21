package com.suport.api.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.suport.api.domain.Client;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.repository.ClientRepository;
import com.suport.api.utils.ClientModelTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ClientControllerIT {
    
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @LocalServerPort
    private int port;
    
    private String getBaseUrl() {
      return "http://localhost:" + port + "/clients";
    }
    
    private  Client createClientInDatabase(){
    return  clientRepository.save(ClientModelTest.createClientValidWithAddress());
    
    }
    
    private <T> HttpEntity<T> jsonEntity(T body) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      return new HttpEntity<>(body, headers);
    }
    
    @Test
    @DisplayName("FindAll: should return list of client")
    void findAll_ReturnListOfClient_when_successful() {
        Client savedClient = createClientInDatabase();

        ResponseEntity<List<ClientResponseDTO>> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ClientResponseDTO>>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ClientResponseDTO> clientList = response.getBody();

        Assertions.assertThat(clientList)
        .isNotNull()
        .isNotEmpty()
        .anyMatch(dto -> dto.id().equals(savedClient.getId()));
    }

    @Test
    @DisplayName("FindById: should return client when id exists")
    void findById_ReturnAnClient_when_successful() {
        Client savedClient = createClientInDatabase();

        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl() + "/" +savedClient.getId(), 
            HttpMethod.GET, 
            null,  
            new ParameterizedTypeReference<ClientResponseDTO>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isEqualTo(savedClient.getId());
        Assertions.assertThat(client.name()).isEqualTo(savedClient.getName());
        Assertions.assertThat(client.email()).isEqualTo(savedClient.getEmail());
        
    }

    @Test
    @DisplayName("FindById: should return BAD_REQUEST when id does not exist")
    void findById_ReturnAnThrowBadRequestException_when_idNotExists() {
        ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
             getBaseUrl() +"/" + 99999999l, 
            HttpMethod.GET, 
            null,  
            new ParameterizedTypeReference<ClientResponseDTO>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
    }

     @Test
    @DisplayName("Save: should return created client")
    void save_ReturnAddress_when_successful() {
          ClientRequestCreateDTO clientResquestDTO = ClientModelTest.createClientResquestDTO();

         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.TEXT_PLAIN);

         ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            jsonEntity(clientResquestDTO), 
            new ParameterizedTypeReference<ClientResponseDTO>() {
            
         });

         Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

         ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isNotNull();
        Assertions.assertThat(client.name()).isEqualTo(clientResquestDTO.name());
        Assertions.assertThat(client.email()).isEqualTo(clientResquestDTO.email());

     
    }

    @Test
    @DisplayName("Update: should return updated task")
    void update_ReturnAddress_when_successful() {
         Client savedClient = createClientInDatabase();
         savedClient.setName("Draven");

         ResponseEntity<ClientResponseDTO> response = testRestTemplate.exchange(
            getBaseUrl()+"/" + savedClient.getId(),
            HttpMethod.PUT, 
            jsonEntity(savedClient), 
            new ParameterizedTypeReference<ClientResponseDTO>() {
         });

         Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

         ClientResponseDTO client = response.getBody();

        Assertions.assertThat(client.id()).isNotNull();
        Assertions.assertThat(client.id()).isEqualTo(savedClient.getId());
        Assertions.assertThat(client.name()).isEqualTo(savedClient.getName());

    }

    @Test
     @DisplayName("Delete: should delete client by id")
      void delete_DeleteClientAndReturnNoContent_when_successful() {
         Client savedClient = createClientInDatabase();

         ResponseEntity<Void> response = testRestTemplate.exchange(
            getBaseUrl() + "/" + savedClient.getId(),
            HttpMethod.DELETE, 
            null, 
            new ParameterizedTypeReference<Void>() {
            
         });

         Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }




}
