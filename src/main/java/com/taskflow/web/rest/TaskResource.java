package com.taskflow.web.rest;

import com.taskflow.entity.Task;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.service.TaskService;
import com.taskflow.utils.Response;
import com.taskflow.web.dto.TaskDTO;
import com.taskflow.web.mapper.TaskDtoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskResource {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping("{id}/request-change")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @SecurityUtils.getCurrentUserLogin() == @taskService.getTaskCreator(#id))")
    public ResponseEntity<Object> requestChangeTask(@PathVariable("id") Long id) throws ResourceNotFoundException {
        taskService.requestChangeTask(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Mono<Task>> createTask(@RequestBody TaskDTO task) throws ResourceNotFoundException {
        Task save = taskService.save(TaskDtoMapper.mapToEntity(task));
        return ResponseEntity.ok(Mono.just(save));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@ParameterObject Pageable pageable) {
        Page<Task> all = taskService.findAll(pageable);
        List<Task> content = all.getContent();
        return ResponseEntity.ok(content.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList());
    }

}
