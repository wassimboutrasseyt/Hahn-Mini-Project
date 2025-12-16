package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository;

import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long ownerId);
}
