package com.taskflow.web.mapper;

import com.taskflow.entity.Tag;
import com.taskflow.web.dto.TagDto;

public class TagDtoMapper {

    private TagDtoMapper() {
    }

    public static Tag mapToEntity(TagDto tag) {
        return Tag.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static TagDto mapToDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
