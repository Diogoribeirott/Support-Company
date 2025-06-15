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

import com.suport.api.domain.Address;
import com.suport.api.dtos.AddressDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.repository.AddressRepository;
import com.suport.api.utils.AddressTests;

@ExtendWith(SpringExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepositoryMock;

    @BeforeEach
    void setUp(){
        Address addressValidWithId = AddressTests.createAddressValidWithId();
        List<Address> listAddresses = List.of(addressValidWithId);

        BDDMockito.when(addressRepositoryMock.findById(ArgumentMatchers.any(Long.class)))
            .thenReturn(Optional.of(addressValidWithId));

        BDDMockito.when(addressRepositoryMock.findAll())
            .thenReturn(listAddresses);

        BDDMockito.when(addressRepositoryMock.save(ArgumentMatchers.any(Address.class)))
            .thenReturn(addressValidWithId);
        

        BDDMockito.doNothing().when(addressRepositoryMock).delete(addressValidWithId);

    }


    @Test
    @DisplayName("Delete by id: delete address by id when successful ")
    void delete_deleteAddressAndReturnNoContent_when_Sucessful() {

        Assertions.assertThatCode(() -> addressService.delete(1l)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("Find by id: find address by id when successful ")
    void findById_ReturnAnAddress_when_sucessful() {

        Address addressValidWithId = AddressTests.createAddressValidWithId();
        Address address = addressService.findByIdOrThrowBadRequestException(5l);

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();
        Assertions.assertThat(address.getStreet()).isEqualTo(addressValidWithId.getStreet());
        Assertions.assertThat(address.getNumber()).isEqualTo(addressValidWithId.getNumber());
        Assertions.assertThat(address.getCity()).isEqualTo(addressValidWithId.getCity());
        Assertions.assertThat(address.getState()).isEqualTo(addressValidWithId.getState());
        Assertions.assertThat(address.getPostalCode()).isEqualTo(addressValidWithId.getPostalCode());
        Assertions.assertThat(address.getComplement()).isEqualTo(addressValidWithId.getComplement());
        Assertions.assertThat(address.getDistrict()).isEqualTo(addressValidWithId.getDistrict());

    }

    @Test
    @DisplayName("when id does not exist: throw bad request exception ")
    void findById_ReturnthrowBadRequestException_when_idNotExits() {

        BDDMockito.when(addressRepositoryMock.findById(ArgumentMatchers.any()))
            .thenThrow(new BadRequestException("Address not found"));

            BadRequestException exception = assertThrows(BadRequestException.class, () ->addressService.findByIdOrThrowBadRequestException(5l));

            Assertions.assertThat(exception.getMessage()).isEqualTo("Address not found");

    }

    @Test
    @DisplayName("findALL: return list of addreses")
    void findAll_ReturnListOfAddress_when_sucessful() {

        Address addressValidWithId = AddressTests.createAddressValidWithId();
        List<Address> listAddresses = addressService.findAll();

        Assertions.assertThat(listAddresses).isNotEmpty();
        Assertions.assertThat(listAddresses.get(0)).isEqualTo(addressValidWithId);
    }

    @Test
    @DisplayName("Save: save addressDTO and return an address")
    void save_returnAddress_when_sucessfull() {

        AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();

       Address address = addressService.save(addressDTOValid);

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();
      
    }

    @Test
    @DisplayName("Update: update address with addressDTO and long id, return an address")
    void update_returnAddress_when_sucessfull() {
        AddressDTO addressDTOValid = AddressTests.createAddressDTOValid();
        Address address = addressService.update(addressDTOValid, 5l);

        Assertions.assertThat(address).isNotNull();
        Assertions.assertThat(address.getId()).isNotNull();

    }

}
