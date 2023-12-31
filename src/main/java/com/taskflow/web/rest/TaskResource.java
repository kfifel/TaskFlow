package com.taskflow.web.rest;

import com.taskflow.entity.Task;
import com.taskflow.entity.enums.TaskStatus;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.service.TaskService;
import com.taskflow.web.dto.TaskDTO;
import com.taskflow.web.dto.UserTaskDto;
import com.taskflow.web.mapper.TaskDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskResource {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskDTO task) throws ResourceNotFoundException {
        Task save = taskService.save(TaskDtoMapper.mapToEntity(task));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(save);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@ParameterObject Pageable pageable) {
        Page<Task> all = taskService.findAll(pageable);
        List<Task> content = all.getContent();
        return ResponseEntity.ok(content.stream().map(TaskDtoMapper::mapToDto).toList());
    }

    @PostMapping("{id}/request-change")
    public ResponseEntity<Object> requestChangeTask(@PathVariable("id") Long id) throws ResourceNotFoundException {
        taskService.requestChangeTask(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/change-status/{status}")
    public ResponseEntity<Object> changeStatus(@PathVariable("id") Long id, @PathVariable("status") TaskStatus status) throws ResourceNotFoundException {
        taskService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/assign/{userId}")
    public ResponseEntity<Object> assignTask(@PathVariable("id") Long id, @PathVariable("userId")  Long userId) throws ResourceNotFoundException {
        taskService.assignTask(id, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/detach")
    public ResponseEntity<Object> assignTask(@PathVariable("id") Long id, @RequestBody String comment) throws ResourceNotFoundException {
        taskService.detach(id, comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("overview")
//    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<UserTaskDto>> getOverviewOfAssignedTasks(
            @Param("filterStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterStartDate,
            @Param("filterStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterEndDate
            ) {
        List<UserTaskDto> userTaskDto = taskService.getOverviewOfAssignedTasks(filterStartDate, filterEndDate);
        return ResponseEntity.ok().body(userTaskDto);
    }
}
