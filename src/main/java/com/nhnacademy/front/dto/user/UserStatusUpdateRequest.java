package com.nhnacademy.front.dto.user;


import com.nhnacademy.front.dto.auth.UserStatus;

public record UserStatusUpdateRequest(
        UserStatus status
) {
}
