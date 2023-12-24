package com.taskflow.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName (String token);

    String generateToken (UserDetails userDetails);

    Authentication getAuthentication(String jwt);

    boolean isTokenValid (String token, UserDetails userDetails);

    boolean isTokenValid (String token);
}