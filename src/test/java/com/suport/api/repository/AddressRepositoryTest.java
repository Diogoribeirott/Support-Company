package com.suport.api.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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

    @Test
    @DisplayName("Save: creates address when successful")
    void save_createsAddress_whenSuccessful() {
        Address addressValid = AddressModelTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();
        Assertions.assertThat(address.getStreet()).isEqualTo(addressValid.getStreet());
        Assertions.assertThat(address.getNumber()).isEqualTo(addressValid.getNumber());
        Assertions.assertThat(address.getCity()).isEqualTo(addressValid.getCity());
        Assertions.assertThat(address.getState()).isEqualTo(addressValid.getState());
        Assertions.assertThat(address.getPostalCode()).isEqualTo(addressValid.getPostalCode());
        Assertions.assertThat(address.getComplement()).isEqualTo(addressValid.getComplement());
        Assertions.assertThat(address.getDistrict()).isEqualTo(addressValid.getDistrict());
    }

    @Test
    @DisplayName("FindAll: returns a list of addresses when successful")
    void findAll_returnsAllAddresses_whenSuccessful() {
        Address addressValid = AddressModelTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        List<Address> addressList = addressRepository.findAll();

        Assertions.assertThat(addressList).isNotEmpty();
        Assertions.assertThat(addressList).hasSize(1);
        Assertions.assertThat(addressList.getFirst().getId()).isEqualTo(address.getId());
        Assertions.assertThat(addressList.getFirst().getStreet()).isEqualTo(addressValid.getStreet());
    }

    @Test
    @DisplayName("Update: updates address when successful")
    void update_updatesAddress_whenSuccessful() {
        Address addressValid = AddressModelTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        address.setCity("New York");
        address.setNumber("223");

        Address addressUpdated = addressRepository.save(address);

        Assertions.assertThat(addressUpdated).isNotNull();
        Assertions.assertThat(addressUpdated.getId()).isEqualTo(address.getId());
        Assertions.assertThat(addressUpdated.getStreet()).isEqualTo(address.getStreet());
        Assertions.assertThat(addressUpdated.getNumber()).isEqualTo("223");
        Assertions.assertThat(addressUpdated.getCity()).isEqualTo("New York");
        Assertions.assertThat(addressUpdated.getState()).isEqualTo(address.getState());
        Assertions.assertThat(addressUpdated.getPostalCode()).isEqualTo(address.getPostalCode());
        Assertions.assertThat(addressUpdated.getComplement()).isEqualTo(address.getComplement());
        Assertions.assertThat(addressUpdated.getDistrict()).isEqualTo(address.getDistrict());
    }

    @Test
    @DisplayName("FindById: returns address when ID exists")
    void findById_returnsAddress_whenIdExists() {
        Address addressValid = AddressModelTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        Optional<Address> addressOptional = addressRepository.findById(address.getId());

        Assertions.assertThat(addressOptional).isPresent().contains(address);

        Address foundAddress = addressOptional.get();
        Assertions.assertThat(foundAddress.getId()).isEqualTo(address.getId());
        Assertions.assertThat(foundAddress.getStreet()).isEqualTo(address.getStreet());
        Assertions.assertThat(foundAddress.getCity()).isEqualTo(address.getCity());
        Assertions.assertThat(foundAddress.getNumber()).isEqualTo(address.getNumber());
        Assertions.assertThat(foundAddress.getState()).isEqualTo(address.getState());
        Assertions.assertThat(foundAddress.getPostalCode()).isEqualTo(address.getPostalCode());
        Assertions.assertThat(foundAddress.getComplement()).isEqualTo(address.getComplement());
        Assertions.assertThat(foundAddress.getDistrict()).isEqualTo(address.getDistrict());
    }

    @Test
    @DisplayName("Delete: deletes address when successful")
    void delete_deletesAddress_whenSuccessful() {
        Address addressValid = AddressModelTests.createAddressValid();
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
