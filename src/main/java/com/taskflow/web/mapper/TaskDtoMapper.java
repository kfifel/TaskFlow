package com.taskflow.web.mapper;

import com.taskflow.entity.Task;
import com.taskflow.web.dto.TaskDTO;

public class TaskDtoMapper {

    public static Task mapToEntity(TaskDTO task) {
        return Task.builder()
                .id(task.getId())
                .description(task.getDescription())
                .expDate(task.getExpDate())
                .completed(task.isCompleted())
                .hasChanged(task.isHasChanged())
                .tags(task.getTags().stream().map(TagDtoMapper::mapToEntity).toList())
                .build();
    }
}
