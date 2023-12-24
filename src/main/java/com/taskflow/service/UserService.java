package com.taskflow.service;

import com.taskflow.entity.User;
import com.taskflow.utils.ValidationException;
import com.taskflow.web.dto.RoleDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    User save(User user) throws ValidationException;
    User update(User user);
    User delete(User user);
    List<User> findAll();

    void revokeRole(Long id, List<RoleDto> roles) throws ValidationException;

    void assigneRole(Long id, List<RoleDto> roles) throws ValidationException;

    List<String> getAuthorities();

    UserDetailsService userDetailsService();

    User findByUsername(String username);
}
