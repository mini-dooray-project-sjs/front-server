package com.nhnacademy.front.dto.user;


public record UserUpdateRequest(
        String email,
        String password,
        UserStatus status
) {
}
