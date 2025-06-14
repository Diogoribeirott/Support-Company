package com.suport.api.service;

import com.suport.api.domain.Address;
import com.suport.api.dtos.AddressDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.repository.AddressRepository;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Address save(AddressDTO addressDTO){
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
       return addressRepository.save(address);
    }

    public Address findByIdOrThrowBadRequestException(Long id) {
      return addressRepository.findById(id).orElseThrow(
        () -> new BadRequestException("Address not found"));
    }

    public List<Address> findAll(){
        return addressRepository.findAll();
    }

    @Transactional
    public Address update(AddressDTO body, Long id){
        Address address = findByIdOrThrowBadRequestException(id);
        BeanUtils.copyProperties(body, address);
        return addressRepository.save(address);
    }

    @Transactional
    public void delete(Long id){
        findByIdOrThrowBadRequestException(id);
        addressRepository.deleteById(id);
    }

}
