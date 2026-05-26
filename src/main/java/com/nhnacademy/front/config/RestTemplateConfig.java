package com.nhnacademy.front.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate=new RestTemplate();

        // 요청을 가로채서 세션 쿠키를 추가하는 인터셉터 등록
        restTemplate.getInterceptors().add((request, body, execution) -> {

            // 브라우저에서 현재 요청을 가져옴
            ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if(attributes!=null) {
                HttpServletRequest req=attributes.getRequest();
                Cookie[] cookies=req.getCookies();

                // 쿠키 목록에서 "SESSION" 쿠키를 찾아서 RestTemplate 요청 헤더에 추가
                if(cookies!=null) {
                    for(Cookie cookie: cookies) {
                        if("SESSION".equals(cookie.getName())) {
                            request.getHeaders().add("Cookie", "SESSION="+cookie.getValue());
                            break;
                        }
                    }
                }
            }

            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
