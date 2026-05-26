package com.nhnacademy.front.dto.milestone;


public record MilestoneUpdateRequest(
        String name,
        MilestoneStatus status
) {
}
