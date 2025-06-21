package com.suport.api.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Address;
import com.suport.api.utils.AddressModelTests;

@DataJpaTest
@DisplayName("Tests for the Address repository")
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private Address addressValid;

    @BeforeEach
    void setUp() {
       addressValid = addressRepository.save(AddressModelTests.createAddressValid());
    }

    @Test
    @DisplayName("Save: creates address when successful")
    void save_createsAddress_whenSuccessful() {
        Address address = addressRepository.save(addressValid);
        
        Assertions.assertThat(address)
              .isNotNull()
              .usingRecursiveComparison()
              .ignoringFields("id")
              .isEqualTo(addressValid);
    }

    @Test
    @DisplayName("FindAll: returns a list of addresses when successful")
    void findAll_returnsAllAddresses_whenSuccessful() {
        Address address = addressRepository.save(addressValid);

        List<Address> addressList = addressRepository.findAll();

        Assertions.assertThat(addressList).isNotEmpty().isNotNull();
        Assertions.assertThat(addressList)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
        .contains(address);
    }

    @Test
    @DisplayName("Update: updates address when successful")
    void update_updatesAddress_whenSuccessful() {
        Address address =addressRepository.save(addressValid);

        address.setCity("New York");
        address.setNumber("223");

        Address addressUpdated = addressRepository.save(address);

        Assertions.assertThat(addressUpdated).isNotNull();
        Assertions.assertThat(addressUpdated.getId()).isEqualTo(address.getId());
        Assertions.assertThat(addressUpdated.getNumber()).isEqualTo("223");
        Assertions.assertThat(addressUpdated.getCity()).isEqualTo("New York");
    }

    @Test
    @DisplayName("FindById: returns address when ID exists")
    void findById_returnsAddress_whenIdExists() {
        Address address = addressRepository.save(addressValid);

        Optional<Address> addressOptional = addressRepository.findById(address.getId());

        Assertions.assertThat(addressOptional).isPresent();
        Assertions.assertThat(addressOptional.get()).usingRecursiveComparison().isEqualTo(address);
    }

    @Test
    @DisplayName("FindById: returns empty when ID does not exist")
    void findById_returnsEmpty_whenIdNotExists() {
        Optional<Address> result = addressRepository.findById(999L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Delete: deletes address when successful")
    void delete_deletesAddress_whenSuccessful() {
        Address address = addressRepository.save(addressValid);

        addressRepository.deleteById(address.getId());
        Optional<Address> addressOptional = addressRepository.findById(address.getId());

        Assertions.assertThat(addressOptional).isEmpty();
    }

    @Test
    @DisplayName("Delete: does nothing when ID does not exist")
    void delete_doesNothing_whenIdDoesNotExist() {
        Assertions.assertThatCode(() -> addressRepository.deleteById(999L))
                  .doesNotThrowAnyException();
    }
}
