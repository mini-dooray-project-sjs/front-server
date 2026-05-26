package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.project_member.ProjectMemberAddRequest;
import com.nhnacademy.front.dto.project_member.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMemberClient {

    private final RestTemplate restTemplate;

    @Value("${api.task.url}")
    private String taskUrl;

    // 프로젝트 멤버 조회
    public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId + "/members", List.class);
    }

    // 프로젝트 멤버 추가
    public ProjectMemberResponse addProjectMember(Long projectId, ProjectMemberAddRequest req) {
        return restTemplate.postForObject(taskUrl+"/projects/"+projectId+"/members", req, ProjectMemberResponse.class);
    }

    // 프로젝트 멤버 삭제
    public void deleteProjectMember(Long projectId, String userId) {
        restTemplate.delete(taskUrl+"/projects/"+projectId+"/members/"+userId);
    }
}
