package com.taskflow.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
public class TaskResource {


    @PostMapping("{id}/request-change")
    public ResponseEntity<Object> requestChangeTask() {

        return null;
    }
}
