package com.taskflow.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserTaskDto {

    private String id;
    private String fullName;
    private String email;
    private List<TaskDTO> tasks;
    private Integer usedTokenCount;
    private Integer percentageCompleted;
    private LocalDate filterStartDate;
    private LocalDate filterEndDate;
}
