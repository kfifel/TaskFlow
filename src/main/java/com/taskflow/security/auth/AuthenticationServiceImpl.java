package com.taskflow.security.auth;

import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.entity.enums.RoleConstant;
import com.taskflow.repository.UserRepository;
import com.taskflow.security.AuthenticationService;
import com.taskflow.security.jwt.JwtService;
import com.taskflow.service.RoleService;
import com.taskflow.service.UserService;
import com.taskflow.utils.ValidationException;
import com.taskflow.web.dto.request.SignInRequest;
import com.taskflow.web.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) throws ValidationException {
        Role roleUser;
        Optional<Role> byName = roleService.findByName(RoleConstant.ROLE_USER.name());
        if(byName.isEmpty())
            roleUser = roleService.save(Role.builder().name(RoleConstant.ROLE_USER.name()).build());
        else
            roleUser = byName.get();
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(List.of(roleUser))
        .build();
        userService.save(user);
        String token = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(token).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow();
        var token = jwtService.generateToken(user);
        return  JwtAuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
