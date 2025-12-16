package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper;

import com.whyboutrasseyt.miniproject.domain.model.Task;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.TaskEntity;

public final class TaskMapper {
    private TaskMapper() {
    }

    public static Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Task(
                entity.getId(),
                entity.getProjectId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                entity.isCompleted()
        );
    }

    public static TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskEntity(
                task.id(),
                task.projectId(),
                task.title(),
                task.description(),
                task.dueDate(),
                task.completed()
        );
    }
}
