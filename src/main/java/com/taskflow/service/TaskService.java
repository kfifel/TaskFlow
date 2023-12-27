package com.taskflow.service;

import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.entity.enums.TaskStatus;
import com.taskflow.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    void requestChangeTask(Long id) throws ResourceNotFoundException;
    Task findById(Long id) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    Page<Task> findAll(Pageable pageable);
    Task save(Task task) throws ResourceNotFoundException;
    User getTaskCreator(Long taskId) throws ResourceNotFoundException;
    void assignTask(Long taskId, Long userId) throws ResourceNotFoundException;
    void changeStatus(Long taskId, TaskStatus status) throws ResourceNotFoundException;
}
