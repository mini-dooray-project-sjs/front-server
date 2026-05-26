package com.nhnacademy.front.dto.project;

import lombok.Builder;

@Builder
public record ProjectResponse(
        Long projectId,
        String name,
        ProjectStatus status,
        boolean admin
) {
}
