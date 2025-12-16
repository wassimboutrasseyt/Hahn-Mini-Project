package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.adapter;

import com.whyboutrasseyt.miniproject.domain.model.User;
import com.whyboutrasseyt.miniproject.domain.repository.UserRepository;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper.UserMapper;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userJpaRepository.save(UserMapper.toEntity(user)));
    }
}
