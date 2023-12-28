package com.taskflow.service.impl;

import com.taskflow.entity.*;
import com.taskflow.entity.enums.StatusRequest;
import com.taskflow.entity.enums.TaskStatus;
import com.taskflow.entity.enums.TokenType;
import com.taskflow.exception.InsufficientTokensException;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.exception.UnauthorizedException;
import com.taskflow.repository.TaskChangeRequestRepository;
import com.taskflow.repository.TaskDetachedHistoryRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.security.SecurityUtils;
import com.taskflow.service.TagService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.taskflow.utils.AppConstants.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TagService tagService;
    private final TaskChangeRequestRepository taskChangeRequestRepository;
    private final TaskDetachedHistoryRepository taskDetachedHistoryRepository;

    @Override
    @Transactional
    public void requestChangeTask(Long id) throws ResourceNotFoundException {
        var principal = getAuthenticatedUser();
        Task task = findById(id);
        canTaskBeChanged(task, principal);

        principal.setNumberOfChangeTokens(principal.getNumberOfChangeTokens() - 1);
        task.setHasChanged(true);
        task.setUser(null);
        taskChangeRequestRepository.save(TaskChangeRequest.builder()
                        .tokenType(TokenType.CHANGE_TOKEN)
                        .dateRequest(LocalDateTime.now())
                        .task(task)
                        .status(StatusRequest.PENDING)
                        .oldOwnerId(task.getUser().getId())
                .build());
        taskRepository.save(task);
    }

    public Task findById(Long id) throws ResourceNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("task", TASK_NOT_FOUND));
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = findById(id);

        if (!task.getCreatedBy().equals(principal)) throw new UnauthorizedException("You can't delete this task");
        taskRepository.delete(task);
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Task save(Task task) throws ResourceNotFoundException {
        User principal = getAuthenticatedUser();
        canTakeBeCreated(task);
        validateTags(task);

        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(principal);
        if (task.getUser() != null)
            task.setAssignedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public User getTaskCreator(Long taskId) throws ResourceNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task", TASK_NOT_FOUND)).getCreatedBy();
    }

    @Override
    public void assignTask(Long taskId, Long userId) throws ResourceNotFoundException {
        Task task = findById(taskId);
        User user = userService.findById(userId);
        canTaskBeAssigned(task);
        task.setUser(user);
        task.setAssignedDate(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void changeStatus(Long taskId, TaskStatus status) throws ResourceNotFoundException {
        Task task = this.findById(taskId);
        if(status == TaskStatus.DONE && task.getDeadline().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Task deadline is passed");
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void detach(Long id, String comment) throws ResourceNotFoundException {
        Task task = findById(id);
        var principal = getAuthenticatedUser();
        canTaskBeDetached(task, principal);
        task.setUser(null);
        principal.setHasDeleteToken(false);
        task.setHasChanged(true);
        taskDetachedHistoryRepository.save(TaskDetachedHistory.builder()
                .comments(comment)
                .task(task)
                .detachedBy(principal)
                .build());
        taskRepository.save(task);
    }

    private void canTaskBeDetached(Task task, User principal) {
        if (task.getUser() == null)
            throw new IllegalArgumentException("This task is not assigned to anyone");

        if (!principal.isHasDeleteToken())
            throw new InsufficientTokensException("You have only one delete token per Month and you have already used it");

        if(task.getStartDate().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Task is already started");
    }

    private void canTaskBeAssigned(Task task) {
        if (task.getDeadline() == null)
            throw new IllegalArgumentException("Task must have an expiration date");

        if (task.getUser() != null) {
            throw new IllegalArgumentException("This task is already assigned to someone");
        }
    }

    private void canTakeBeCreated(Task task) {
        if (task.getStartDate() != null) {
            if (task.getDeadline().toLocalDate().isAfter(LocalDate.now().plusDays(3)))
                throw new IllegalArgumentException("Task scheduling is restricted to 3 days in advance.");
            if (task.getStartDate().isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("The date is in the past !");
        }
    }

    private void validateTags(Task task) {
        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("At least 2 tags is required !");
        }
        List<Tag> tags = tagService.findByNameIn(task.getTags().stream().map(Tag::getName).toList());
        task.setTags(tags);
    }

    private void canTaskBeChanged(Task task, User principal) {
        if(task.getUser() == null)
            throw new IllegalArgumentException("This task is not assigned to anyone");
        if (task.isHasChanged())
            throw new IllegalArgumentException("This task has already been changed");
        if (principal.getNumberOfChangeTokens() <= 0)
            throw new InsufficientTokensException("You don't have enough change tokens");
    }

    private User getAuthenticatedUser() {
        String username = SecurityUtils.getCurrentUserLogin();
        if (username == null)
            throw new UnauthorizedException("User Not Found In the Context");
        return userService.findByUsername(username);
    }
}
