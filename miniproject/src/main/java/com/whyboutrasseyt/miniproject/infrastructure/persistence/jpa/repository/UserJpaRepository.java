package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository;

import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
