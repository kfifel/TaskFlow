package com.taskflow.web.mapper;

import com.taskflow.entity.Role;
import com.taskflow.entity.User;
import com.taskflow.web.dto.request.UserRequestDto;
import com.taskflow.web.dto.response.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {



    private UserDtoMapper() {
    }

    public static User toEntity(UserRequestDto userDto) {
        List<Role> roles = new ArrayList<>();
        if(userDto.getAuthorities() != null){
            for (String role : userDto.getAuthorities()) {
                roles.add(Role.builder().name(role).build());
            }
        }
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(roles)
                .build();
    }

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .verifiedAt(user.getVerifiedAt())
                .numberOfChangeTokens(user.getNumberOfChangeTokens())
                .toDoubleTokenChaneDate(user.getToDoubleTokenChaneDate())
                .hasDeleteToken(user.isHasDeleteToken())
                .authorities(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
