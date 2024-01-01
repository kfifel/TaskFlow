package com.taskflow.web.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeResource {

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @securityUtils.getCurrentUserLogin() == @taskService.getTaskCreator(#id).getUsername())")
    public ResponseEntity<String> home(
            @PathVariable("userId") Long id
    ) {
        return ResponseEntity.ok("Hello World");
    }
}
