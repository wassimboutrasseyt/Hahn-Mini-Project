package com.whyboutrasseyt.miniproject.presentation.response;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        long totalTasks,
        long completedTasks,
        double progressPercentage
) {
}
