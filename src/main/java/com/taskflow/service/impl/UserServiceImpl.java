package com.taskflow.service.impl;

import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.repository.RoleRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.taskflow.utils.AppConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public User save(User user) throws ValidationException {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ValidationException(new CustomError("Email","Email already exists"));

        final List <Role> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roleService.findByName(role.getName()).ifPresent(roles::add));
        user.setRoles(roles);
        user.setNumberOfChangeTokens(2);
        user.setHasDeleteToken(true);
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
    @Transactional
    public User assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            final int[] someExist = {0};
            roles.forEach(roleDto ->
                roleService.findByName(roleDto.getName())
                    .ifPresentOrElse(
                        role -> {
                            if (user.getRoles().contains(role))
                                someExist[0] = 1;
                            roleList.add(role);
                            },
                        () -> roleList.add(Role.builder().name(roleDto.getName()).build())));
            if(someExist[0] == 1)
                throw new ValidationException(CustomError.builder().field("roles").message("User already has some of specified roles.").build());
            roleRepository.saveAll(roleList);
            user.getRoles().addAll(roleList);
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException("user", USER_NOT_FOUND);
    }

    @Override
    public List<String> getAuthorities() {
        return roleService.getALlRoles().stream().map(Role::getName).toList();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public User findById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userId", USER_NOT_FOUND));
    }
}
