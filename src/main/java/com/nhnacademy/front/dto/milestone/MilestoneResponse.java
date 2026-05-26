package com.nhnacademy.front.dto.milestone;

import lombok.Builder;

@Builder
public record MilestoneResponse(
        Long milestoneId,
        String name,
        MilestoneStatus status
) {
}
