package com.taskflow.service;

import com.taskflow.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> findByNameInOrSave(List<String> names);
}
