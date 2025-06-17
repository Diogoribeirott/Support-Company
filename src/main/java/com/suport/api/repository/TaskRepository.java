package com.suport.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suport.api.domain.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{

    

}
