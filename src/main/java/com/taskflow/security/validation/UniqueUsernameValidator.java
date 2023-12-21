package com.taskflow.security.validation;

import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserRepository repository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        User user = repository.findByEmail(username).orElse(null);
        return username != null && user == null;
    }
}
