package com.whyboutrasseyt.miniproject.api.response;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        Long projectId,
        String title,
        String description,
        LocalDate dueDate,
        boolean completed
) {
}
