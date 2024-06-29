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

        // 모든 경로에 대해 CORS를 허용하며, 특정 원본 (http://localhost:3000)에서만 요청을 허용합니다.
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie") // 클라이언트로 Set-Cookie 헤더를 노출합니다.
                .allowedOrigins("http://localhost:3000"); // 허용할 원본(도메인)을 설정합니다.
    }
}
