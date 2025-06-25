package com.suport.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.suport.api.domain.UserModel;

public interface UserModelRepository extends JpaRepository<UserModel,Long> {
   Optional<UserDetails> findByLogin(String login);
}
