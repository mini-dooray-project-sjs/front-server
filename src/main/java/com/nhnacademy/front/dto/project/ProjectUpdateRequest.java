package com.nhnacademy.front.dto.project;


public record ProjectUpdateRequest(
        String name,
        ProjectStatus status
) {}
