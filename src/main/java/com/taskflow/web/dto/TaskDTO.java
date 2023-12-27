package com.taskflow.web.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.taskflow.entity.Task}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO implements Serializable {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private LocalDateTime startDate;

    private LocalDateTime assignedDate;


    private boolean completed;

    private boolean hasChanged;

    @NotEmpty
    private List<TagDto> tags;

    private String createdBy;

    private String assignedTo;
}