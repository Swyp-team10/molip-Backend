package org.example.shallweeatbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 설정을 관리하는 클래스입니다.
 */
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://www.molip.site") // 여러 출처 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메소드를 설정합니다.
                .allowedHeaders("*") // 모든 헤더를 허용합니다.
                .allowCredentials(true) // 자격 증명(쿠키 등)을 허용합니다.
                .exposedHeaders("Set-Cookie", "access") // 클라이언트로 노출할 헤더를 설정합니다.
                .maxAge(3600);
    }
}