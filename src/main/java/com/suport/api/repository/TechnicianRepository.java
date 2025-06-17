package com.suport.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suport.api.domain.Technician;

public interface TechnicianRepository  extends JpaRepository<Technician,Long> {

}
