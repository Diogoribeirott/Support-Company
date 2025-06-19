package com.suport.api.service;

import com.suport.api.domain.Address;
import com.suport.api.dtos.request.AddressRequestDTO;
import com.suport.api.dtos.response.AddressResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.mappers.AddressMapper;
import com.suport.api.repository.AddressRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // =============================
    // CREATE
    // =============================
    @Transactional
    public AddressResponseDTO save(AddressRequestDTO dto) {
        Address address = new Address();
        BeanUtils.copyProperties(dto, address);

        Address saved = addressRepository.save(address);
        return AddressMapper.createAddressResponseDTO(saved);
    }

    // =============================
    // READ
    // =============================
    @Transactional(readOnly = true)
    public List<AddressResponseDTO> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(AddressMapper::createAddressResponseDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Address findByIdOrThrowBadRequestException(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No address found with the provided ID: " + id));
    }

    // =============================
    // UPDATE
    // =============================
    @Transactional
    public AddressResponseDTO update(AddressRequestDTO dto, Long id) {
        Address address = findByIdOrThrowBadRequestException(id);
        BeanUtils.copyProperties(dto, address);

        Address updated = addressRepository.save(address);
        return AddressMapper.createAddressResponseDTO(updated);
    }

    // =============================
    // DELETE
    // =============================
    @Transactional
    public void deleteById(Long id) {
        findByIdOrThrowBadRequestException(id);
        addressRepository.deleteById(id);
    }
}
