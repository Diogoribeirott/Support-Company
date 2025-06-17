package com.suport.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suport.api.domain.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

}
