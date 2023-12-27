package com.taskflow.web.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.taskflow.entity.Tag}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto implements Serializable {
    Long id;
    String name;
}