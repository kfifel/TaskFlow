package com.taskflow.service;

import com.taskflow.entity.Tag;
import com.taskflow.exception.ResourceNotFoundException;

import java.util.List;

public interface TagService {

    List<Tag> findByNameIn(List<String> names);
}
