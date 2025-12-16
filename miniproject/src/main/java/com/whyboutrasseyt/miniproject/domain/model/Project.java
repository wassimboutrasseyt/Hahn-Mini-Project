package com.whyboutrasseyt.miniproject.domain.model;

import java.util.Objects;

public record Project(
        Long id,
        Long ownerId,
        String title,
        String description
) {
    public Project {
        Objects.requireNonNull(ownerId, "ownerId is required");
        Objects.requireNonNull(title, "title is required");
    }
}
