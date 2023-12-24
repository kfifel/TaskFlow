package com.taskflow.service;

import com.taskflow.entity.Task;
import com.taskflow.exception.ResourceNotFoundException;

public interface TaskService {

    void requestChangeTask(Long id) throws ResourceNotFoundException;
    Task findById(Long id) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
