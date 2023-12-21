package com.taskflow.service.impl;

import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.RoleService;
import com.taskflow.service.UserService;
import com.taskflow.utils.CustomError;
import com.taskflow.utils.ValidationException;
import com.taskflow.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public User save(User user) throws ValidationException {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ValidationException(new CustomError("Email","Email already exists"));

        final List <Role> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roleService.findByName(role.getName()).ifPresent(roles::add));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User delete(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void revokeRole(Long id, List<RoleDto> roles) throws ValidationException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            roles.forEach(roleDto -> roleService.
                findByName(roleDto.getName()).ifPresent(roleList::add));

            if (new HashSet<>(user.getRoles()).containsAll(roleList)) {
                user.getRoles().removeAll(roleList);
                userRepository.save(user);
            } else {
                throw new ValidationException(CustomError.builder()
                        .field("roles")
                        .message("User does not have all specified roles.")
                        .build());
            }
        }
        else {
            throw new ValidationException(CustomError.builder()
                    .field("user id")
                    .message("User does not exist")
                    .build());
        }
    }

    @Override
    public void assigneRole(Long id, List<RoleDto> roles) throws ValidationException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            roles.forEach(roleDto -> roleService.findByName(roleDto.getName()).ifPresent(roleList::add));
            if(user.getRoles().stream().anyMatch(roleList::contains)) {
                throw new ValidationException(CustomError.builder()
                        .field("roles")
                        .message("User already has some of specified roles.")
                        .build());
            }
            user.getRoles().addAll(roleList);
            userRepository.save(user);
        }
    }

    @Override
    public List<String> getAuthorities() {
        return roleService.getALlRoles().stream().map(Role::getName).toList();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
