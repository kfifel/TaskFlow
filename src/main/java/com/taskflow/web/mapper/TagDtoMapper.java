package com.taskflow.web.mapper;

import com.taskflow.entity.Tag;
import com.taskflow.web.dto.TagDto;

public class TagDtoMapper {

    public static Tag mapToEntity(TagDto tag) {
        return Tag.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
