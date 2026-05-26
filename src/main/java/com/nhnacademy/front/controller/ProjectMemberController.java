package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.ProjectMemberClient;
import com.nhnacademy.front.dto.project_member.ProjectMemberAddRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/members")
public class ProjectMemberController {

    private final ProjectMemberClient projectMemberClient;

    // 프로젝트 멤버 추가
    @PostMapping
    public String addProjectMember(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute ProjectMemberAddRequest req
            ) {
        projectMemberClient.addProjectMember(projectId, req);

        return "redirect:/projects/" + projectId;
    }

    // 프로젝트 멤버 삭제
    @DeleteMapping("/{user-id}")
    public String removeProjectMember(
            @PathVariable("project-id") Long projectId,
            @PathVariable("user-id") String userId
    ) {
        projectMemberClient.deleteProjectMember(projectId, userId);

        return "redirect:/projects/" + projectId;
    }
}
