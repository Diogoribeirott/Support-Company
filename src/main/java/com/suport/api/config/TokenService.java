package com.suport.api.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.suport.api.domain.UserModel;
import com.suport.api.exceptions.TokenGenerationException;
import com.suport.api.exceptions.TokenValidationException;

@Service
public class TokenService {

    @Value("${api.security.token.secretKey}")
    private String secretKey;

    // =============================
    // CREATE TOKEN
    // =============================

    public String createToken(UserModel userModel){
        try {

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("SuportTI")
                    .withSubject(userModel.getLogin())
                    .withExpiresAt(generateExpirationTime())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new TokenGenerationException("Error generating JWT token", e);
        }

    }

    private Instant generateExpirationTime(){
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));

    }

    // =============================
    // TOKEN VALIDATION 
    // =============================

   public String tokenValidation(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
          return  JWT.require(algorithm)
                .withIssuer("SuportTI")
                .build()
                .verify(token)
                .getSubject();
            
        } catch (JWTVerificationException e) {
            throw new TokenValidationException("Invalid JWT token",e);
        }

   }

}
