package com.whyboutrasseyt.miniproject.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description
) {
}
