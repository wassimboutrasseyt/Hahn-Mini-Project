package com.whyboutrasseyt.miniproject.application.command;

import java.time.LocalDate;

public record CreateTaskCommand(
        Long projectId,
        String title,
        String description,
        LocalDate dueDate
) {
}
