package com.suport.api.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.suport.api.domain.Client;
import com.suport.api.domain.Task;
import com.suport.api.domain.Technician;
import com.suport.api.dtos.request.TaskRequestCreateDTO;
import com.suport.api.dtos.response.TaskResponseDTO;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.mappers.TaskMapper;
import com.suport.api.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ClientService clientService;
    private final TechnicianService technicianService;

    public TaskService(
        TaskRepository taskRepository,
        ClientService clientService,
        TechnicianService technicianService){

        this.taskRepository = taskRepository;
        this.clientService = clientService;
        this.technicianService = technicianService;

    }

    // =============================
    // CREATE
    // =============================
    @Transactional
    public TaskResponseDTO save(TaskRequestCreateDTO dto) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task, "clientId", "technicianIds"  );
        
        Client client = clientService.findByIdOrThrowBadRequestException( dto.clientId());
        task.setClient(client);

       Set<Technician> technicians = dto.technicianIds() != null 
       ? dto.technicianIds().stream()
       .map(technicianService::findByIdOrThrowBadRequestException)
       .collect(Collectors.toSet()) :  Set.of();
        
       task.setTechnicians(technicians);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponseDTO(savedTask);
    }

    // =============================
    // READ
    // =============================
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Task findByIdOrThrowBadRequestException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No Task found with the provided ID: " + id));
    }

    // =============================
    // UPDATE
    // =============================
    @Transactional
    public TaskResponseDTO update(TaskRequestCreateDTO dto, Long id) {
        Task task = findByIdOrThrowBadRequestException(id);
        BeanUtils.copyProperties(dto, task);

        Client client = clientService.findByIdOrThrowBadRequestException( dto.clientId());
        task.setClient(client);

       Set<Technician> technicians = dto.technicianIds() != null 
       ? dto.technicianIds().stream()
       .map(technicianService::findByIdOrThrowBadRequestException)
       .collect(Collectors.toSet()) :  Set.of();
        
        task.setTechnicians(technicians);


        taskRepository.save(task);
        return TaskMapper.toResponseDTO(task);
    }

    // =============================
    // DELETE
    // =============================
    @Transactional
    public void delete(Long id) {
        findByIdOrThrowBadRequestException(id);
        taskRepository.deleteById(id);
    }


}
