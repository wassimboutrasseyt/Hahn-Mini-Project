package com.whyboutrasseyt.miniproject.domain.model;

import java.util.Objects;

public record User(
        Long id,
        String email,
        String passwordHash,
        String role
) {
    public User {
        Objects.requireNonNull(email, "email is required");
        Objects.requireNonNull(passwordHash, "passwordHash is required");
        Objects.requireNonNull(role, "role is required");
    }
}
