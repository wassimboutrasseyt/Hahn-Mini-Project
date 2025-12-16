package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper;

import com.whyboutrasseyt.miniproject.domain.model.Project;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.ProjectEntity;

public final class ProjectMapper {
    private ProjectMapper() {
    }

    public static Project toDomain(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Project(
                entity.getId(),
                entity.getOwnerId(),
                entity.getTitle(),
                entity.getDescription()
        );
    }

    public static ProjectEntity toEntity(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectEntity(
                project.id(),
                project.ownerId(),
                project.title(),
                project.description()
        );
    }
}
