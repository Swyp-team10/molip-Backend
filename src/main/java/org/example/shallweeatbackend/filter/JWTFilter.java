package org.example.shallweeatbackend.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.util.JWTUtil;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWTFilter 클래스는 HTTP 요청을 가로채서 JWT 토큰을 검증하고 사용자 인증을 처리하는 필터입니다.
 * OncePerRequestFilter를 상속하여 각 요청마다 한 번씩 실행됩니다.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스

    // 요청을 필터링하여 JWT 토큰을 검증하고, 사용자 인증을 설정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인, 만료 시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // access 토큰이 만료된 경우, 클라이언트에게 401 에러 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access token expired.");
            return;
        }

        // 토큰이 access인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            // 유효하지 않은 access 토큰일 경우, 클라이언트에게 400 에러 응답
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid access token");
            return;
        }

        // providerId, role 값을 획득
        String providerId = jwtUtil.getProviderId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserDTO userDTO = new UserDTO();
        userDTO.setProviderId(providerId);
        userDTO.setRole(role);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
