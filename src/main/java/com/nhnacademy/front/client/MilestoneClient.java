package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.front.dto.milestone.MilestoneResponse;
import com.nhnacademy.front.dto.milestone.MilestoneUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MilestoneClient {

    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskUrl;

    // 프로젝트의 마일스톤 목록 조회
    public List<MilestoneResponse> getMilestones(Long projectId) {
        return restTemplate.getForObject(taskUrl+"/projects/" + projectId + "/milestones", List.class);
    }

    // 프로젝트의 마일스톤 단건 조회
    public MilestoneResponse getMilestone(Long projectId, Long milestoneId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId + "/milestones/" + milestoneId, MilestoneResponse.class);
    }

    // 프로젝트의 마일스톤 생성
    public void createMilestone(Long projectId, MilestoneCreateRequest req) {
        restTemplate.postForObject(taskUrl + "/projects/" + projectId + "/milestones", req, MilestoneResponse.class);
    }

    // 프로젝트의 마일스톤 수정
    public void updateMilestone(Long projectId, Long milestoneId, MilestoneUpdateRequest req) {
        restTemplate.postForObject(taskUrl + "/projects/" + projectId + "/milestones/" + milestoneId, req, MilestoneResponse.class);
    }

    // 프로젝트의 마일스톤 삭제
    public void deleteMilestone(Long projectId, Long milestoneId) {
        restTemplate.delete(taskUrl + "/projects/" + projectId + "/milestones/" + milestoneId);
    }
}
