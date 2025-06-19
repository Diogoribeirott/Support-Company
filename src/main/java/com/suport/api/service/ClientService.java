package com.suport.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suport.api.domain.Address;
import com.suport.api.domain.Client;
import com.suport.api.dtos.request.ClientRequestCreateDTO;
import com.suport.api.dtos.request.ClientRequestUpdateDTO;
import com.suport.api.dtos.response.ClientResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.mappers.ClientMapper;
import com.suport.api.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // =============================
    // CREATE
    // =============================
    @Transactional
    public ClientResponseDTO save(ClientRequestCreateDTO dto) {
        Client client = new Client();
        BeanUtils.copyProperties(dto, client, "address");

        if (dto.address() != null) {
            Address address = new Address();
            BeanUtils.copyProperties(dto.address(), address);
            client.setAddress(address);
        }

        Client savedClient = clientRepository.save(client);
        return ClientMapper.toResponseDTO(savedClient);
    }

    // =============================
    // READ
    // =============================
    
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll()
            .stream()
            .map(ClientMapper::toResponseDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public Client findByIdOrThrowBadRequestException(Long id) {
        return clientRepository.findById(id).orElseThrow(
            () -> new BadRequestException("No client found with the provided ID: " + id)
        );
    }

    // =============================
    // UPDATE
    // =============================
    @Transactional
    public ClientResponseDTO update(ClientRequestUpdateDTO dto, Long id) {
        Client client = findByIdOrThrowBadRequestException(id);
        BeanUtils.copyProperties(dto, client, "address");

        if (dto.address() != null) {
            Address address = new Address();
            BeanUtils.copyProperties(dto.address(), address);
            client.setAddress(address);
        }

        Client updatedClient = clientRepository.save(client);
        return ClientMapper.toResponseDTO(updatedClient);
    }

    // =============================
    // DELETE
    // =============================
    @Transactional
    public void deleteById(Long id) {
        Client client = findByIdOrThrowBadRequestException(id);
        clientRepository.delete(client);
    }
}
