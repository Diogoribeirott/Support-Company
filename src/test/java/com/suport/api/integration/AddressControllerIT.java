package com.suport.api.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.suport.api.domain.Address;
import com.suport.api.dtos.AddressDTO;
import com.suport.api.repository.AddressRepository;
import com.suport.api.utils.AddressTests;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AddressControllerIT {

    @Autowired
    private TestRestTemplate testrestTemplate;

    @Autowired
    private AddressRepository addressRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("FindALL: return list of addreses")
    void findAll_ReturnListOfAddress_when_sucessful() {

        Address addressValid = addressRepository.save(AddressTests.createAddressValid());

        ResponseEntity<List<Address>> response = testrestTemplate.exchange("/address",HttpMethod.GET,null, new ParameterizedTypeReference<List<Address>>(){});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Address> listAddresses = response.getBody();

        Assertions.assertThat(listAddresses).isNotEmpty();
        Assertions.assertThat(listAddresses.get(0).getComplement()).isEqualTo(addressValid.getComplement());

    }

    @Test
    @DisplayName("Find by id: find address by id when successful ")
    void findById_ReturnAnAddress_when_sucessful() {
        Address addressValid = addressRepository.save(AddressTests.createAddressValid());

        String url = "/address/"+ addressValid.getId();

        ResponseEntity<Address> response = testrestTemplate.exchange( url,
        HttpMethod.GET,
        null,
         new ParameterizedTypeReference<Address>(){});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Address address = response.getBody();

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
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        String url = "/address/9999999";

        ResponseEntity<Address> response = testrestTemplate.exchange( url,
        HttpMethod.GET,
        null,
         new ParameterizedTypeReference<Address>(){});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    @DisplayName("Save: save addressDTO and return an address")
    void save_returnAddress_when_sucessfull() {

        AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();

        String url = "/address";    

        ResponseEntity<Address> response = testrestTemplate.exchange( url,
        HttpMethod.POST,
        new HttpEntity<>(addressDTOValid),
        new ParameterizedTypeReference<Address>(){});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Address address = response.getBody();

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();
      
    }

    @Test
    @DisplayName("Update: update address with addressDTO and long id, return an address")
    void update_returnAddress_when_sucessfull() {

        Address createAddressValidWithId = addressRepository.save(AddressTests.createAddressValid());
        createAddressValidWithId.setStreet("alpheneiros");

        String url = "/address/" + createAddressValidWithId.getId() ;   

        ResponseEntity<Address> response = testrestTemplate.exchange( url,
        HttpMethod.PUT,
        new HttpEntity<>(createAddressValidWithId),
        new ParameterizedTypeReference<Address>(){});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Address address = response.getBody();

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();
        Assertions.assertThat(address.getStreet()).isEqualTo("alpheneiros");

    }

     @Test
    @DisplayName("Delete by id: delete address by id when successful ")
    void delete_deleteAddressAndReturnNoContent_when_Sucessful() {

       Address createAddressValidWithId = addressRepository.save(AddressTests.createAddressValid());

        String url = "/address/" + createAddressValidWithId.getId() ;   

        ResponseEntity<Void> response = testrestTemplate.exchange( url,
        HttpMethod.DELETE,
        null,
        new ParameterizedTypeReference<Void>(){});

        boolean exists = addressRepository.existsById(createAddressValidWithId.getId());

        Assertions.assertThat(exists).isFalse();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }



}
