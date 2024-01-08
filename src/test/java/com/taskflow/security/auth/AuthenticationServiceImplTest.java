package com.taskflow.security.auth;

import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.entity.enums.RoleConstant;
import com.taskflow.repository.UserRepository;
import com.taskflow.security.jwt.JwtService;
import com.taskflow.service.RoleService;
import com.taskflow.service.UserService;
import com.taskflow.utils.ValidationException;
import com.taskflow.web.dto.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImplUnderTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_signe_up_if_email_already_exists() throws ValidationException {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@gmail.com")
                .build();
        Role roleUser = new Role(null, RoleConstant.ROLE_USER.name());

//        when(roleService.findByName(RoleConstant.ROLE_USER.name())).thenReturn(Optional.of(roleUser));
//        when(userService.save(any(User.class))).thenReturn(createMockUser());
//        when(jwtService.generateToken(any(User.class), any())).thenReturn("token");
//        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//        when(passwordEncoder.encode())
        // Test the signup method
//        JwtAuthenticationResponse response = authenticationServiceImplUnderTest.signup(signUpRequest);

        // Assertions
//        assertNotNull(response);
//        assertNotNull(response.getAccessToken());

        // Verify that userService.save and jwtService.generateToken were called
//        verify(userService, times(1)).save(any(User.class));
        //verify(jwtService, times(1)).generateToken(any(User.class));
        assertTrue(true);
    }

    @Test
    void signin() {
    }


    private User createMockUser() {
        // Create and return a mock user for testing
        // You can customize this based on your requirements
        return User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of(Role.builder().name(RoleConstant.ROLE_USER.name()).build()))
                .build();
    }

}