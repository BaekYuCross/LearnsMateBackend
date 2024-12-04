package intbyte4.learnsmate.security;

import intbyte4.learnsmate.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final AdminService userService;
    private final JwtUtil jwtUtil;
    private final List<String> excludeUrl = Arrays.asList(
            "/admin/verification-email/**",
            "/actuator/health",
            "/admin/verification-email/password",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/admin/password",
            "/users/login"
    );

    public JwtFilter(AdminService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrl.stream()
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;
        log.info("Request URL: {}", request.getRequestURL());

        // 쿠키에서 토큰 가져오기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    log.info("Token found in cookies: {}", token);
                    break;
                }
            }
        }

        // 토큰 검증
        if (token == null || !jwtUtil.validateToken(token)) {
            log.warn("유효하지 않은 토큰 또는 토큰이 없습니다. Token: {}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답
            return; // 필터 체인 진행 중단
        }

        // 토큰 만료 시간 확인 (추가 디버깅 코드)
        try {
            Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
            log.info("Token Expiration: {}, Current Time: {}", expirationDate, new Date());
        } catch (Exception e) {
            log.error("토큰 만료 시간 확인 중 오류 발생", e);
        }

        // 인증 객체 생성 및 SecurityContext 설정
        Authentication authentication = jwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication 설정 완료: {}", authentication);

        filterChain.doFilter(request, response);
    }
}
