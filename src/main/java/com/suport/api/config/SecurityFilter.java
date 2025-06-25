package com.suport.api.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.suport.api.domain.UserModel;
import com.suport.api.exceptions.BadRequestException;
import com.suport.api.exceptions.TokenValidationException;
import com.suport.api.repository.UserModelRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final UserModelRepository userModelRepository;
    private final TokenService tokenService;

    public SecurityFilter(UserModelRepository userModelRepository,TokenService tokenService ) {
        this.userModelRepository = userModelRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        var token = recoverToken(request);
        if (token != null) {

            var login =  tokenService.tokenValidation(token);

             UserDetails user = userModelRepository.findByLogin(login)
            .orElseThrow(() -> new TokenValidationException("User not found for token"));

            var authentication = new UsernamePasswordAuthenticationToken(user,null , user.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    private String recoverToken(HttpServletRequest request) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.replace("Bearer ", "");
            }
            return null;
        }
}