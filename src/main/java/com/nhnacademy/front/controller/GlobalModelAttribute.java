package com.nhnacademy.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

/**
 * 모든 컨트롤러 요청에서 공통적으로 사용자 정보를 모델에 추가하는 클래스
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final ObjectMapper objectMapper;

    @ModelAttribute
    public void addUserInfoToModel(
            @CookieValue(value="accessToken", required=false) String accessToken,
            Model model
    ) {
        if(accessToken!=null && !accessToken.isEmpty()) {
            try {
                String[] parts=accessToken.split("\\.");
                if(parts.length>1) {
                    String payload=new String(Base64.getUrlDecoder().decode(parts[1]));

                    Map<String, Object> claims=objectMapper.readValue(payload, Map.class);

                    String userId=(String) claims.get("sub");
                    String userRole=(String) claims.get("role");
                    String role=userRole.toLowerCase().contains("user")? "유저":"관리자";

                    model.addAttribute("userId", userId);
                    model.addAttribute("role", role);
                }
            } catch(Exception e) {
                log.warn("JWT 토큰에서 사용자 정보 추출 실패: {}", e.getMessage());
            }
        }
    }
}
