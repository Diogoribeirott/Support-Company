package com.suport.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TechnicianRequestDTO;
import com.suport.api.dtos.response.TechnicianResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.mappers.TechnicianMapper;
import com.suport.api.repository.TechnicianRepository;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    // =============================
    // CREATE
    // =============================
    @Transactional
    public TechnicianResponseDTO save(TechnicianRequestDTO dto) {
        Technician technician = new Technician();
        BeanUtils.copyProperties(dto, technician);

        Technician savedTechnician = technicianRepository.save(technician);
        return TechnicianMapper.toResponseDTO(savedTechnician);
    }

    // =============================
    // READ
    // =============================
    @Transactional(readOnly = true)
    public List<TechnicianResponseDTO> findAll() {
        return technicianRepository.findAll().stream()
                .map(TechnicianMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Technician findByIdOrThrowBadRequestException(Long id) {
        return technicianRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No Technician found with the provided ID: " + id));
    }

    // =============================
    // UPDATE
    // =============================
    @Transactional
    public TechnicianResponseDTO update(TechnicianRequestDTO dto, Long id) {
        Technician technician = findByIdOrThrowBadRequestException(id);
        BeanUtils.copyProperties(dto, technician);

        technicianRepository.save(technician);
        return TechnicianMapper.toResponseDTO(technician);
    }

    // =============================
    // DELETE
    // =============================
    @Transactional
    public void delete(Long id) {
        findByIdOrThrowBadRequestException(id);
        technicianRepository.deleteById(id);
    }
}
