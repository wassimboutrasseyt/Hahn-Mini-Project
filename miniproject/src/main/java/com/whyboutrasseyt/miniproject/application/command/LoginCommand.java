package com.whyboutrasseyt.miniproject.application.command;

public record LoginCommand(
        String email,
        String password
) {
}
