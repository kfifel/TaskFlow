package com.taskflow.service.impl;

import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.exception.InsufficientTokensException;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.exception.UnauthorizedException;
import com.taskflow.repository.TaskRepository;
import com.taskflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public void requestChangeTask(Long id) throws ResourceNotFoundException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = findById(id);

        if(task.isHasChanged())
            throw new IllegalArgumentException("This task has already been changed");
        if(principal.getNumberOfChangeTokens() <= 0)
            throw new InsufficientTokensException("You don't have enough change tokens");

        principal.setNumberOfChangeTokens(principal.getNumberOfChangeTokens() - 1);
        task.setHasChanged(true);
        task.setUser(null);
        taskRepository.save(task);
    }

    public Task findById(Long id) throws ResourceNotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task", "Task not found"));
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = findById(id);

        if(!task.getCreatedBy().equals(principal))
            throw new UnauthorizedException("You can't delete this task");
        taskRepository.delete(task);
    }
}
