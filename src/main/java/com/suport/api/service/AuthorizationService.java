package com.suport.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.suport.api.repository.UserModelRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserModelRepository userModelRepository;

    public AuthorizationService(UserModelRepository userModelRepository){
        this.userModelRepository = userModelRepository;
    }
    
    // =============================
    // USER IN DATABASE
    // =============================
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
      return userModelRepository.findByLogin(login)
      .orElseThrow(() -> new UsernameNotFoundException("User not found" + login));
    }

}
