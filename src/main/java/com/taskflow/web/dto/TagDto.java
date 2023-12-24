package com.taskflow.web.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.taskflow.entity.Tag}
 */
@Value
public class TagDto implements Serializable {
    Long id;
    String name;
}