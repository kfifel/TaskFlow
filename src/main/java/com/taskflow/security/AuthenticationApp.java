package com.taskflow.security;

import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class AuthenticationApp {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordUtil;
    @Bean
    public User userConnected() {
        Optional<User> userOptional = userRepository.findById(1L);

        return userOptional.orElseGet(() -> {
            User newUser = User.builder()
                    .email("khalid.fifel@gmail.com")
                    .firstName("khalid")
                    .lastName("fifel")
                    .password(passwordUtil.encode("password"))
                    .build();
            return userRepository.save(newUser);
        });
    }
}
