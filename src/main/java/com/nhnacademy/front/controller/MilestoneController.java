package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.MilestoneClient;
import com.nhnacademy.front.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.front.dto.milestone.MilestoneResponse;
import com.nhnacademy.front.dto.milestone.MilestoneUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/milestones")
public class MilestoneController {

    private final MilestoneClient milestoneClient;

    // 마일스톤 단건 조회
    @GetMapping("/{milestone-id}")
    public String getMilestone(
            @PathVariable("project-id") Long projectId,
            @PathVariable("milestone-id") Long milestoneId,
            Model model
    ) {
        MilestoneResponse resp = milestoneClient.getMilestone(projectId, milestoneId);
        model.addAttribute("milestone", resp);
        model.addAttribute("projectId", projectId);

        return "milestone/detail";
    }

    // 마일스톤 생성
    @PostMapping
    public String createMilestone(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute MilestoneCreateRequest req
    ) {
        milestoneClient.createMilestone(projectId, req);

        return "redirect:/projects/{project-id}";
    }

    // 마일스톤 수정
    @PostMapping("/{milestone-id}")
    public String updateMilestone(
            @PathVariable("project-id") Long projectId,
            @PathVariable("milestone-id") Long milestoneId,
            @ModelAttribute MilestoneUpdateRequest req
    ) {
        milestoneClient.updateMilestone(projectId, milestoneId, req);

        return "redirect:/projects/{project-id}/milestones/{milestone-id}";
    }

    // 마일스톤 삭제
    @DeleteMapping("/{milestone-id}")
    public String deleteMilestone(
            @PathVariable("project-id") Long projectId,
            @PathVariable("milestone-id") Long milestoneId
    ) {
        milestoneClient.deleteMilestone(projectId, milestoneId);

        return "redirect:/projects/{project-id}";
    }
}
