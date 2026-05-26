package com.nhnacademy.front.dto.user;

import com.nhnacademy.front.dto.auth.UserRole;
import com.nhnacademy.front.dto.auth.UserStatus;
import lombok.Builder;

@Builder
public record UserResponse(
        String userId,
        String email,
        UserStatus status,
        UserRole role
) {
}
