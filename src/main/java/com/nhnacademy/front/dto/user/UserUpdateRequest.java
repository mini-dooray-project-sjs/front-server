package com.nhnacademy.front.dto.user;


import com.nhnacademy.front.dto.auth.UserStatus;

public record UserUpdateRequest(
        String email,
        String password,
        UserStatus status
) {
}
