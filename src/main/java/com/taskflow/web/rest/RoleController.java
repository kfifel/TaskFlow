package com.taskflow.web.rest;

import com.taskflow.entity.Role;
import com.taskflow.web.dto.RoleDto;
import com.taskflow.web.mapper.RoleDtoMapper;
import com.taskflow.service.RoleService;
import com.taskflow.utils.Response;
import com.taskflow.utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Response<RoleDto>> save(@Valid @RequestBody RoleDto roleDto){
        Response<RoleDto> response = new Response<>();
        Role role = RoleDtoMapper.toRole(roleDto);
        try {
            response.setResult(RoleDtoMapper.toDto(roleService.save(role)));
            response.setMessage("Role has been added successfully");
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (ValidationException e)
        {
            response.setMessage("Role has not been added");
            response.setErrors(List.of(e.getCustomError()));
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleService.getALlRoles();
        List<RoleDto> roleDtos = new ArrayList<>();

        for (Role role : roles) {
            RoleDto roleDto = RoleDtoMapper.toDto(role);
            roleDtos.add(roleDto);
        }
        return new ResponseEntity<>(roleDtos,HttpStatus.OK);
    }
    @GetMapping("/{name}")
    public ResponseEntity<Response<RoleDto>> getRoleWithName(@PathVariable("name") String name)
    {
        Role role = new Role();
        role.setName(name);
        Optional<Role> roleOptional = roleService.findByName(name);
        if(roleOptional.isPresent()) {
            RoleDto dto = RoleDtoMapper.toDto(roleOptional.get());
            return ResponseEntity.ok().body(Response.<RoleDto>builder()
                    .result(dto)
                    .message("Role has been found")
                    .build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Response.<RoleDto>builder()
                        .message("Role has not been found")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id)
    {
        try
        {
            roleService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable("id") long id, @RequestParam String name)
    {
        try {
            Role role1 = roleService.findById(id);
            role1.setName(name);
            return new ResponseEntity<>(RoleDtoMapper.toDto(roleService.save(role1)),HttpStatus.OK);
        }catch (ValidationException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
