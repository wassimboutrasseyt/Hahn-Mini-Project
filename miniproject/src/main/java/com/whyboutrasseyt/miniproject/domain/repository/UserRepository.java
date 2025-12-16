package com.whyboutrasseyt.miniproject.domain.repository;

import com.whyboutrasseyt.miniproject.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    User save(User user);
}
