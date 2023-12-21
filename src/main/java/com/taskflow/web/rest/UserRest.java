package com.taskflow.web.rest;

import com.taskflow.entity.User;
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
public class UserRest {

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
    public ResponseEntity<Response<UserResponseDto>> save(@RequestBody @Valid UserRequestDto userDto){
        Response<UserResponseDto> response = new Response<>();
        try {
            User user = UserDtoMapper.toEntity(userDto);
            response.setResult(UserDtoMapper.toDto(userService.save(user)));
            response.setMessage("User has been saved successfully");
        } catch (ValidationException e) {
            response.setErrors(List.of(e.getCustomError()));
            response.setMessage("User has not been saved");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<String>> getAuthorities() {
        return ResponseEntity.ok().body(userService.getAuthorities());
    }

    @PostMapping("/revokeRole/{id}")
    public ResponseEntity<Response<?>> revokeRole(@PathVariable("id") Long id, @RequestBody List<RoleDto> roles){
        Response<?> response = new Response<>();
        try {
            userService.revokeRole(id, roles);
            response.setMessage("Role has been revoked successfully");
        } catch (ValidationException e) {
            response.setErrors(List.of(e.getCustomError()));
            response.setMessage("Role has not been revoked");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/assigneRole/{id}")
    public ResponseEntity<Response<?>> assigneRole(@PathVariable("id") Long id, @RequestBody List<RoleDto> roles){
        Response<?> response = new Response<>();
        try {
            userService.assigneRole(id, roles);
            response.setMessage("Role has been assigned successfully");
        } catch (ValidationException e) {
            response.setErrors(List.of(e.getCustomError()));
            response.setMessage("Role has not been assigned");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
