package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.*;
import com.nhnacademy.front.dto.auth.LoginUserResponse;
import com.nhnacademy.front.dto.milestone.MilestoneResponse;
import com.nhnacademy.front.dto.project.ProjectCreateRequest;
import com.nhnacademy.front.dto.project.ProjectResponse;
import com.nhnacademy.front.dto.project.ProjectStatus;
import com.nhnacademy.front.dto.project.ProjectUpdateRequest;
import com.nhnacademy.front.dto.project_member.ProjectMemberResponse;
import com.nhnacademy.front.dto.tag.TagResponse;
import com.nhnacademy.front.dto.task.TaskSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final AuthClient authClient;
    private final ProjectClient projectClient;
    private final ProjectMemberClient projectMemberClient;
    private final MilestoneClient milestoneClient;
    private final TagClient tagClient;
    private final TaskClient taskClient;

    // 프로젝트 목록 페이지
    @GetMapping
    public String projectList(
            Model model,
            @CookieValue(name="SESSION", required = false) String sessionId
    ) {
        if(sessionId==null || sessionId.isEmpty()) {
            return "redirect:/login";
        }

        LoginUserResponse userResponse=authClient.getLoginUser();
        String userId=userResponse.userId();
        String role=userResponse.role().name();


        List<ProjectResponse> resp=projectClient.getProjects();
        log.info("Project List: {}", resp);

        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        model.addAttribute("projects", resp);

        return "project/list";
    }

    // 프로젝트 상세 페이지
    @GetMapping("/{project-id}")
    public String getProjectDetail(
            Model model,
            @PathVariable("project-id") Long projectId
    ) {
        ProjectResponse projectResponse=projectClient.getProject(projectId);
        List<ProjectMemberResponse> members=projectMemberClient.getProjectMembers(projectId);
        List<MilestoneResponse> milestones=milestoneClient.getMilestones(projectId);
        List<TagResponse> tags=tagClient.getTags(projectId);
        List<TaskSummaryResponse> tasks=taskClient.getTasks(projectId);


        model.addAttribute("project", projectResponse);
        model.addAttribute("members", members);
        model.addAttribute("milestones", milestones);
        model.addAttribute("tags", tags);
        model.addAttribute("tasks", tasks);

        return "project/detail";
    }

    // 프로젝트 생성
    @PostMapping
    public String createProject(
            @ModelAttribute ProjectCreateRequest req
    ) {
        projectClient.createProject(req);

        return "redirect:/projects";
    }

    // 프로젝트 수정
    @PostMapping("/{project-id}")
    public String updateProject(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute ProjectUpdateRequest req
    ) {
        projectClient.updateProject(projectId, req);

        return "redirect:/projects/{project-id}";
    }

    // 프로젝트 삭제
    @DeleteMapping("/{project-id}")
    public String dleteProject(
            @PathVariable("project-id") Long projectId
    ) {
        projectClient.deleteProject(projectId);

        return "redirect:/projects";
    }
}
