package com.taskflow.service.impl;

import com.taskflow.entity.Tag;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.repository.TagRepository;
import com.taskflow.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> findByNameIn(List<String> names) {
        List<String> tags = new ArrayList<>(names);
        List<Tag> existingTags = tagRepository.findByNameIn(names);
        if (existingTags.size() == tags.size()) {
            return existingTags;
        }
        existingTags.forEach(tag -> tags.remove(tag.getName()));
        tags.forEach(name -> existingTags.add(tagRepository.save(Tag.builder().name(name).build())));
        return existingTags;
    }
}
