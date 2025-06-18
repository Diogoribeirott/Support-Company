package com.suport.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suport.api.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{

    

}
