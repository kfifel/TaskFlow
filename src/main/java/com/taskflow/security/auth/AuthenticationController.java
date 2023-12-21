package com.taskflow.security.auth;

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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SignInRequest credential) throws ValidationException {
        JwtAuthenticationResponse result = authenticationService.signin(credential);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest register) throws ValidationException {
        JwtAuthenticationResponse result = authenticationService.signup(register);

        return ResponseEntity.ok(result);
    }
}
