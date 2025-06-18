package com.suport.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suport.api.domain.Technician;

@Repository
public interface TechnicianRepository  extends JpaRepository<Technician,Long> {

}
