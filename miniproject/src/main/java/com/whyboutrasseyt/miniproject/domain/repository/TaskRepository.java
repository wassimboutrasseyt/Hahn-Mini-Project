package com.whyboutrasseyt.miniproject.domain.repository;

import com.whyboutrasseyt.miniproject.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findByProjectId(Long projectId);

    void deleteById(Long id);

    long countByProjectId(Long projectId);

    long countCompletedByProjectId(Long projectId);
}
