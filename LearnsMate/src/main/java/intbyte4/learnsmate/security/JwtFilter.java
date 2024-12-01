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
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final AdminService userService;
    private final JwtUtil jwtUtil;
    private final List<String> excludeUrl = Arrays.asList(
            "/admin/verification-email/**", "/actuator/health",
            "/admin/verification-email/password", "/swagger-ui.html",
            "/swagger-ui/index.html", "/admin/password"
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;

        // 쿠키에서 토큰 가져오기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰 유효성 검사 및 인증 객체 설정
        if (token != null && jwtUtil.validateToken(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication 설정 완료: {}", authentication);
        } else {
            // DEBUG 레벨로 로그를 기록하거나 로그 생략
            log.debug("유효하지 않은 토큰이거나 토큰이 없습니다.");
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }
}
