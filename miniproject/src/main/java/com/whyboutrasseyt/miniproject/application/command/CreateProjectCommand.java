package com.whyboutrasseyt.miniproject.application.command;

public record CreateProjectCommand(
        Long ownerId,
        String title,
        String description
) {
}
