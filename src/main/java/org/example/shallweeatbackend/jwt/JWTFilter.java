package org.example.shallweeatbackend.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWTFilter 클래스는 HTTP 요청을 가로채서 JWT 토큰을 검증하고 사용자 인증을 처리하는 필터입니다.
 * OncePerRequestFilter를 상속하여 각 요청마다 한 번씩 실행됩니다.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // HTTP 요청 헤더에서 access 토큰을 가져옴
        String accessToken = request.getHeader("access");

        // access 토큰이 없으면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // access 토큰이 만료되었는지 검사
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // access 토큰이 만료된 경우 처리
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // access 토큰의 카테고리를 가져옴
        String category = jwtUtil.getCategory(accessToken);

        // 카테고리가 'access'가 아니면 유효하지 않은 토큰으로 처리
        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // access 토큰에서 공급자 ID와 역할을 가져옴
        String providerId = jwtUtil.getProviderId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // UserDTO 객체를 생성하고 공급자 ID와 역할을 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setProviderId(providerId);
        userDTO.setRole(role);

        // CustomOAuth2User 객체를 생성하고 UserDTO를 이용해 초기화
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // 인증 토큰을 생성하여 SecurityContext에 설정
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
