package com.suport.api.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.suport.api.domain.Address;
import com.suport.api.utils.AddressTests;

@DataJpaTest
@DisplayName("Tests for the repository")
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Save: creates address when successful")
    void save_createsAddress_when_Sucessful(){

        Address addressValid = AddressTests.createAddressValid();
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
    @DisplayName("Update: Update address when successful")
    void Update_UpdateAddress_when_Sucessful(){

        Address addressValid = AddressTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        address.setCity("New York");
        address.setNumber("223");

        Address addressUpdate = addressRepository.save(address);

        Assertions.assertThat(addressUpdate).isNotNull();
        Assertions.assertThat(addressUpdate.getId()).isNotNull();
        Assertions.assertThat(addressUpdate.getStreet()).isEqualTo(address.getStreet());
        Assertions.assertThat(addressUpdate.getNumber()).isEqualTo("223");
        Assertions.assertThat(addressUpdate.getCity()).isEqualTo("New York");
        Assertions.assertThat(addressUpdate.getState()).isEqualTo(address.getState());
        Assertions.assertThat(addressUpdate.getPostalCode()).isEqualTo(address.getPostalCode());
        Assertions.assertThat(addressUpdate.getComplement()).isEqualTo(address.getComplement());
        Assertions.assertThat(addressUpdate.getDistrict()).isEqualTo(address.getDistrict());

    }

    @Test
    @DisplayName("FindById: findById address when successful")
    void findById_findByIdAddress_when_Sucessful(){

        Address addressValid = AddressTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        Optional<Address> addressOptional = addressRepository.findById(address.getId());
        
        Assertions.assertThat(addressOptional.isEmpty()).isFalse();
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
    @DisplayName("delete: delete address when successful")
    void delete_deleteAddress_when_Sucessful(){

        Address addressValid = AddressTests.createAddressValid();
        Address address = addressRepository.save(addressValid);

        addressRepository.deleteById(address.getId());
        Optional<Address> AddressOptional = addressRepository.findById(address.getId());

        Assertions.assertThat(AddressOptional.isEmpty()).isTrue();

    }

    @Test
    @DisplayName("delete: does nothing when ID does not exist")
    void delete_doesNothing_when_NotSucessful(){

        Assertions.assertThatCode(() ->addressRepository.deleteById(1l)).doesNotThrowAnyException();

    }

}
