package com.taskflow.web.mapper;

import com.taskflow.entity.Task;
import com.taskflow.web.dto.TaskDTO;

public class TaskDtoMapper {

    private TaskDtoMapper() {
    }

    public static Task mapToEntity(TaskDTO task) {
        return Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .completed(task.isCompleted())
                .hasChanged(task.isHasChanged())
                .tags(task.getTags().stream().map(TagDtoMapper::mapToEntity).toList())
                .startDate(task.getStartDate())
                .build();
    }

    public static TaskDTO mapToDto(Task task) {
        String assignedTo = task.getUser() == null ? null : task.getUser().getFirstName() + " " + task.getUser().getLastName();
        String createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .assignedDate(task.getAssignedDate())
                .completed(task.isCompleted())
                .hasChanged(task.isHasChanged())
                .tags(task.getTags().stream().map(TagDtoMapper::mapToDto).toList())
                .assignedTo(assignedTo)
                .createdBy(createdBy)
                .startDate(task.getStartDate())
                .build();
    }
}
