package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.project.ProjectCreateRequest;
import com.nhnacademy.front.dto.project.ProjectResponse;
import com.nhnacademy.front.dto.project.ProjectUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectClient {

    private final RestTemplate restTemplate;



    @Value("${api.task.url}")
    private String taskUrl;

    // 프로젝트 목록 조회
    public List<ProjectResponse> getProjects() {
        try {
            return restTemplate.exchange(
                    taskUrl + "/projects",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProjectResponse>>() {
                    }
            ).getBody();
        } catch(Exception e) {
            return List.of();
        }
    }

    // 프로젝트 단건 조회
    public ProjectResponse getProject(Long projectId) {
        return restTemplate.getForObject(taskUrl + "/projects/" + projectId, ProjectResponse.class);
    }

    // 프로젝트 생성
    public ProjectResponse createProject(ProjectCreateRequest req) {
        return restTemplate.postForObject(taskUrl+"/projects", req, ProjectResponse.class);
    }

    // 프로젝트 수정
    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest req) {
        return restTemplate.postForObject(taskUrl+"/projects/"+projectId, req, ProjectResponse.class);
    }

    // 프로젝트 삭제
    public void deleteProject(Long projectId) {
        restTemplate.delete(taskUrl+"/projects/"+projectId);
    }
}
