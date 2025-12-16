package com.whyboutrasseyt.miniproject.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public record Task(
        Long id,
        Long projectId,
        String title,
        String description,
        LocalDate dueDate,
        boolean completed
) {
    public Task {
        Objects.requireNonNull(projectId, "projectId is required");
        Objects.requireNonNull(title, "title is required");
    }
}
