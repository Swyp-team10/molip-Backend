package org.example.shallweeatbackend.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.util.JWTUtil;
import org.example.shallweeatbackend.repository.RefreshTokenRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 사용자 로그아웃을 처리하는 필터입니다.
 * "/logout" 엔드포인트로 POST 요청이 들어오면, 유효한 리프레시 토큰을 확인하고 삭제하는 역할을 합니다.
 */
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 요청 URI가 "/logout"이 아니면 필터 체인을 통해 다음 필터로 전달합니다.
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 요청 메서드가 POST가 아니면 필터 체인을 통해 다음 필터로 전달합니다.
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // refresh 토큰을 쿠키에서 가져옵니다.
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            // refresh 토큰이 없을 경우, 클라이언트에게 400 에러 응답
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("리프레시 토큰이 필요합니다.");
            return;
        }

        // refresh 토큰이 만료되었는지 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // refresh 토큰이 만료된 경우, 클라이언트에게 401 에러 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("리프레시 토큰이 만료되었습니다.");
            return;
        }

        // refresh 토큰이 리프레시용인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // 유효하지 않은 리프레시 토큰일 경우, 클라이언트에게 400 에러 응답
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("유효하지 않은 토큰 유형입니다.");
            return;
        }

        // refresh 토큰이 DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refresh);
        if (!isExist) {
            // DB에 저장되어 있지 않은 경우, 클라이언트에게 404 에러 응답
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("리프레시 토큰을 찾을 수 없습니다.");
            return;
        }

        // DB에서 refresh 토큰 삭제
        refreshTokenRepository.deleteByRefreshToken(refresh);

        // 클라이언트에게 refresh 쿠키 삭제 요청
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 클라이언트에게 200 OK 응답
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("로그아웃 되었습니다.");
    }
}
