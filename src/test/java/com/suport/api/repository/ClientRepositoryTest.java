package com.suport.api.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Client;
import com.suport.api.utils.ClientModelTest;

@DataJpaTest
@DisplayName("Tests for the Client repository")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private  Client clientValid;

    @BeforeEach
    void setUp(){
      clientValid = ClientModelTest.createClientValidWithAddress();
    }

    @Test
    @DisplayName("Save: creates client when successful")
    void save_createsClient_whenSuccessful() {
        Client client = clientRepository.save(clientValid);

        Assertions.assertThat(client)
              .isNotNull()
              .usingRecursiveComparison()
              .ignoringFields("id")
              .isEqualTo(clientValid);
    }

    @Test
    @DisplayName("Update: updates client when successful")
    void update_updatesClient_whenSuccessful() {
        Client client = clientRepository.save(clientValid);

        client.setEmail("google@example.com");
        client.setName("Google 2.0");

        Client clientUpdated = clientRepository.save(client);

        Assertions.assertThat(clientUpdated).isNotNull();
        Assertions.assertThat(clientUpdated.getId()).isEqualTo(client.getId());
        Assertions.assertThat(clientUpdated.getEmail()).isEqualTo("google@example.com");
        Assertions.assertThat(clientUpdated.getName()).isEqualTo("Google 2.0");
    }

    @Test
    @DisplayName("FindById: returns client when ID exists")
    void findById_returnsClient_whenIdExists() {
        Client client = clientRepository.save(clientValid);

        Optional<Client> clientOptional = clientRepository.findById(client.getId());

        Assertions.assertThat(clientOptional).isPresent();
        Assertions.assertThat(clientOptional).isNotEmpty();
        Assertions.assertThat(clientOptional.get()).usingRecursiveComparison().isEqualTo(client);
    }

    @Test
    @DisplayName("FindById: returns empty when ID does not exist")
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Client> result = clientRepository.findById(9999999L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("FindAll: returns a list of clients when successful")
    void findAll_returnsAllClients_whenSuccessful() {
        Client client = clientRepository.save(clientValid);

        List<Client> clientList = clientRepository.findAll();

        Assertions.assertThat(clientList).isNotEmpty().isNotNull();
        Assertions.assertThat(clientList)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
        .contains(client);
    }

    @Test
    @DisplayName("Delete: deletes client when successful")
    void delete_deletesClient_whenSuccessful() {
        Client client = clientRepository.save(clientValid);

        clientRepository.deleteById(client.getId());

        Optional<Client> clientOptional = clientRepository.findById(client.getId());
        Assertions.assertThat(clientOptional).isEmpty();
    }

    @Test
    @DisplayName("Delete: does nothing when ID does not exist")
    void delete_doesNothing_whenIdDoesNotExist() {
        Assertions.assertThatCode(() -> clientRepository.deleteById(1L))
                  .doesNotThrowAnyException();
    }
}
