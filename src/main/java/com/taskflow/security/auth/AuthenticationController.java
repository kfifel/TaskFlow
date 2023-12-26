package com.taskflow.security.auth;

import com.taskflow.exception.UnauthorizedException;
import com.taskflow.security.AuthenticationService;
import com.taskflow.utils.ValidationException;
import com.taskflow.web.dto.request.SignInRequest;
import com.taskflow.web.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody @Valid SignInRequest credential) {
        JwtAuthenticationResponse result = authenticationService.signin(credential);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody @Valid SignUpRequest register) throws ValidationException {
        JwtAuthenticationResponse result = authenticationService.signup(register);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request) throws ValidationException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Refresh token is missing");
        }
        String token = authorization.substring(7);
        JwtAuthenticationResponse result = authenticationService.refreshToken(token);
        return ResponseEntity.ok(result);
    }
}
