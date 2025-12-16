package com.whyboutrasseyt.miniproject.domain.repository;

import com.whyboutrasseyt.miniproject.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);

    Optional<Project> findById(Long id);

    List<Project> findByOwnerId(Long ownerId);

    void deleteById(Long id);
}
