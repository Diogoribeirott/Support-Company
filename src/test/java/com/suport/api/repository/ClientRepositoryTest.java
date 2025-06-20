package com.suport.api.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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

    @Test
    @DisplayName("Save: creates client when successful")
    void save_createsClient_whenSuccessful() {
        Client clientValid = ClientModelTest.createClientValidWithAddress();
        Client client = clientRepository.save(clientValid);

        Assertions.assertThat(client).isNotNull();
        Assertions.assertThat(client.getId()).isNotNull();
        Assertions.assertThat(client.getEmail()).isEqualTo(clientValid.getEmail());
        Assertions.assertThat(client.getType()).isEqualTo(clientValid.getType());
    }

    @Test
    @DisplayName("Update: updates client when successful")
    void update_updatesClient_whenSuccessful() {
        Client clientValid = ClientModelTest.createClientValidWithAddress();
        Client client = clientRepository.save(clientValid);

        client.setEmail("google@example.com");
        client.setName("Google 2.0");

        Client clientUpdated = clientRepository.save(client);

        Assertions.assertThat(clientUpdated).isNotNull();
        Assertions.assertThat(clientUpdated.getId()).isEqualTo(client.getId());
        Assertions.assertThat(clientUpdated.getEmail()).isEqualTo("google@example.com");
        Assertions.assertThat(clientUpdated.getName()).isEqualTo("Google 2.0");
        Assertions.assertThat(clientUpdated.getType()).isEqualTo(client.getType());
    }

    @Test
    @DisplayName("FindById: returns client when ID exists")
    void findById_returnsClient_whenIdExists() {
        Client clientValid = ClientModelTest.createClientValidWithAddress();
        Client client = clientRepository.save(clientValid);

        Optional<Client> clientOptional = clientRepository.findById(client.getId());

        Assertions.assertThat(clientOptional).isPresent().contains(client);

        Client foundClient = clientOptional.get();
        Assertions.assertThat(foundClient).isNotNull();
        Assertions.assertThat(foundClient.getId()).isEqualTo(client.getId());
        Assertions.assertThat(foundClient.getType()).isEqualTo(clientValid.getType());
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
        Client clientValid = ClientModelTest.createClientValidWithAddress();
        Client client = clientRepository.save(clientValid);

        List<Client> clientList = clientRepository.findAll();

        Assertions.assertThat(clientList).hasSize(1);
        Assertions.assertThat(clientList).isNotEmpty();
        Assertions.assertThat(clientList.getFirst().getId()).isEqualTo(client.getId());
        Assertions.assertThat(clientList.getFirst().getName()).isEqualTo(client.getName());
    }

    @Test
    @DisplayName("Delete: deletes client when successful")
    void delete_deletesClient_whenSuccessful() {
        Client clientValid = ClientModelTest.createClientValidWithAddress();
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
