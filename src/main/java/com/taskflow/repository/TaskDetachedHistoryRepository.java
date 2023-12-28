package com.taskflow.repository;

import com.taskflow.entity.TaskDetachedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDetachedHistoryRepository extends JpaRepository<TaskDetachedHistory, Long> {
}