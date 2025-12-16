package com.whyboutrasseyt.miniproject.application.service;

import com.whyboutrasseyt.miniproject.application.command.LoginCommand;
import com.whyboutrasseyt.miniproject.application.dto.UserDto;

public interface AuthService {
    String login(LoginCommand command);

    UserDto currentUser(String token);
}
