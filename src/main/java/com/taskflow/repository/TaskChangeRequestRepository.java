package com.taskflow.repository;

import com.taskflow.entity.TaskChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskChangeRequestRepository extends JpaRepository<TaskChangeRequest, Long> {
}