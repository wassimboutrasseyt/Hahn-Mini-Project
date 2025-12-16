package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper;

import com.whyboutrasseyt.miniproject.domain.model.User;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.UserEntity;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole()
        );
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.id(),
                user.email(),
                user.passwordHash(),
                user.role()
        );
    }
}
