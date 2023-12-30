package com.taskflow.web.rest;

import com.taskflow.entity.User;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.web.dto.RoleDto;
import com.taskflow.web.dto.request.UserRequestDto;
import com.taskflow.web.dto.response.UserResponseDto;
import com.taskflow.web.mapper.UserDtoMapper;
import com.taskflow.service.impl.UserServiceImpl;
import com.taskflow.utils.Response;
import com.taskflow.utils.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserServiceImpl userService;


    @GetMapping
    public ResponseEntity<Response<List<UserResponseDto>>> findAll(){
        Response<List<UserResponseDto>> response = new Response<>();
        List<User> users = userService.findAll();
        List<UserResponseDto> usersDto = users.stream().map(UserDtoMapper::toDto).toList();
        response.setResult(usersDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<UserResponseDto>> save(@RequestBody @Valid UserRequestDto userDto) throws ValidationException {
        Response<UserResponseDto> response = new Response<>();
        User user = UserDtoMapper.toEntity(userDto);
        response.setResult(UserDtoMapper.toDto(userService.save(user)));
        response.setMessage("User has been saved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<String>> getAuthorities() {
        return ResponseEntity.ok().body(userService.getAuthorities());
    }

    @PostMapping("/revokeRole/{id}")
    public ResponseEntity<Response<Object>> revokeRole(@PathVariable("id") Long id, @RequestBody List<RoleDto> roles) throws ValidationException {
        Response<Object> response = new Response<>();
        userService.revokeRole(id, roles);
        response.setMessage("Role has been revoked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/assigneRole/{id}")
    public ResponseEntity<UserResponseDto> assigneRole(@PathVariable("id") Long id, @RequestBody List<RoleDto> roles) throws ValidationException, ResourceNotFoundException {
        User user = userService.assigneRole(id, roles);
        return new ResponseEntity<>(UserDtoMapper.toDto(user), HttpStatus.OK);
    }

}
