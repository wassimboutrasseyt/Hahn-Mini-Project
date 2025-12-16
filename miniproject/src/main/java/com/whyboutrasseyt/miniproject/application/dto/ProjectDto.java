package com.whyboutrasseyt.miniproject.application.dto;

public record ProjectDto(
        Long id,
        String title,
        String description,
        long totalTasks,
        long completedTasks
) {
}
