package com.whyboutrasseyt.miniproject.application.dto;

import java.time.LocalDate;

public record TaskDto(
        Long id,
        Long projectId,
        String title,
        String description,
        LocalDate dueDate,
        boolean completed
) {
}
