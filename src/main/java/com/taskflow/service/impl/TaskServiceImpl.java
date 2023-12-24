package com.taskflow.service.impl;

import com.taskflow.entity.Tag;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.exception.InsufficientTokensException;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.exception.UnauthorizedException;
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
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TagService tagService;

    @Override
    @Transactional
    public void requestChangeTask(Long id) throws ResourceNotFoundException {
        Mono<String> username = SecurityUtils.getCurrentUserLogin();
        var principal = userService.findByUsername(username.block());
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

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
    @Override
    public Task save(Task task) throws ResourceNotFoundException {
        taskCannotCreateInThePast(task);
        validateTags(task);
        restrictTaskScheduling(task);
        return taskRepository.save(task);
    }

    private void taskCannotCreateInThePast(Task task) {
        if (task.getAssignedDate() != null && task.getAssignedDate().isBefore(LocalDateTime.now())){
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

    private void restrictTaskScheduling(Task task) {
        LocalDate currentDate = LocalDate.now();
        LocalDate taskExpDate = task.getExpDate().toLocalDate();
        LocalDate maxAllowedExpDate = currentDate.plusDays(3);

        if (taskExpDate.isAfter(maxAllowedExpDate)) {
            throw new IllegalArgumentException("Task scheduling is restricted to 3 days in advance.");
        }
    }
}
