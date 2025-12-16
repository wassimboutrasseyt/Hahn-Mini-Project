package com.whyboutrasseyt.miniproject.presentation.response;

import com.whyboutrasseyt.miniproject.application.dto.UserDto;

public record LoginResponse(String token, UserDto user) {
}
