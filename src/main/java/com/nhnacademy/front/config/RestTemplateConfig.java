package com.nhnacademy.front.config;

import com.nhnacademy.front.client.AuthClient;
import com.nhnacademy.front.dto.auth.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    // RestTemplate 빈에서 AuthClient를 지연 로딩으로 주입받기 위해 ObjectProvider 사용
    private final ObjectProvider<AuthClient> authClientProvider;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate=new RestTemplate();

        // 요청을 가로채서 accessToken을 Authorization 헤더에 자동으로 추가하는 인터셉터 등록
        restTemplate.getInterceptors().add((request, body, execution) -> {

            // 브라우저에서 현재 요청을 가져옴
            ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if(attributes==null) {
                return execution.execute(request, body);
            }

            // 현재 요청에서 accessToken과 refreshToken 쿠키 값을 추출
            HttpServletRequest req=attributes.getRequest();
            String accessToken=getCookieValue(req, "accessToken");
            String refreshToken=getCookieValue(req, "refreshToken");

            // accessToken이 존재하면 Authorization 헤더에 Bearer 토큰으로 추가
            if(accessToken!=null) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
            }

            ClientHttpResponse resp=execution.execute(request, body);

            if(resp.getStatusCode()== HttpStatus.UNAUTHORIZED) {
                log.info("AccessToken 만료 또는 유효하지 않음. RefreshToken으로 토큰 재발급 시도.");

                if(refreshToken==null) {
                    log.warn("RefreshToken 존재하지 않음. 401 Unauthorized 반환.");
                    return resp;
                }

                try {
                    AuthClient authClient=authClientProvider.getObject();
                    ResponseEntity<TokenResponse> tokenResp=authClient.reissueToken(refreshToken);
                    String newAccessToken=Objects.requireNonNull(tokenResp.getBody()).accessToken();

                    HttpServletResponse response=attributes.getResponse();
                    if(response!=null) {
                        ResponseCookie newAccessCookie=ResponseCookie.from("accessToken", newAccessToken)
                                .path("/")
                                .httpOnly(true)
                                .secure(false)
                                .maxAge(60*60)
                                .build();
                        response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
                    }

                    request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer "+newAccessToken);
                    log.info("새로운 AccessToken으로 요청 재시도.");
                    return execution.execute(request, body);
                } catch(Exception ex) {
                    log.error("토큰 재발급 실패: {}", ex.getMessage());
                    return resp;
                }
            }

            return resp;
        });

        return restTemplate;
    }

    // 요청에서 특정 쿠키 값을 추출하는 유틸리티 메서드
    private String getCookieValue(HttpServletRequest req, String cookieName) {
        Cookie[] cookies=req.getCookies();
        if(cookies!=null) {
            for(Cookie cookie: cookies) {
                if(cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
