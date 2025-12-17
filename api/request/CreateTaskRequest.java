package com.whyboutrasseyt.miniproject.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotNull(message = "Project ID is required")
        Long projectId,
        @NotBlank(message = "Title is required")
        String title,
        String description,
        LocalDate dueDate
) {
}
