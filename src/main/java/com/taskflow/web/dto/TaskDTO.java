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
    Long id;
    @NotEmpty
    String title;
    @NotEmpty
    String description;
    @NotNull
    LocalDateTime expDate;
    boolean completed;
    LocalDateTime assignedDate;
    boolean hasChanged;
    List<TagDto> tags;
    String createdByFirstName;
    String createdByLastName;
    String userFirstName;
    String userLastName;
}