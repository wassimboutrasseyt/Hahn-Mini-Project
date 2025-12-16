package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository;

import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByProjectId(Long projectId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndCompletedTrue(Long projectId);
}
