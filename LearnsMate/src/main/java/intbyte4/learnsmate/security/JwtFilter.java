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
//OncePerRequestFilter를 상속받아 doFilterInternal을 오버라이딩 (한번만 실행되는 필터로 요청이 들어올 때마다 필터가 실행)
public class JwtFilter extends OncePerRequestFilter {

    private final AdminService userService;
    private final JwtUtil jwtUtil;  // JWT 토큰을 다루는 JwtUtil 클래스
    private final List<String> excludeUrl  // JWT 토큰 검증을 제외할 URL 패턴 목록
            = Arrays.asList("/admin/verification-email/**"
           , "/admin/verification-email/password", "/swagger-ui.html", "/swagger-ui/index.html"
            , "/admin/password");

    public JwtFilter(AdminService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 이 필터가 실행될지 말지를 결정.  이 메서드는 요청 URL이 `excludeUrl` 목록에 포함되어 있으면, 필터를 건너뛰게 만듦.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrl.stream()
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));
    }

    // JWT 토큰을 검사하고 인증을 처리하는 실제 필터 로직. 만약 유효한 JWT 토큰이 있다면 인증을 처리하고, 없으면 다음 필터로 진행.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JwtFilter 실행 - 쿠키에서 토큰 추출 시도");

        // 쿠키에서 토큰 가져오기
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info("쿠키 이름: {}, 쿠키 값: {}", cookie.getName(), cookie.getValue());
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    log.info("추출된 토큰: {}", token);
                    break;
                }
            }
        } else {
            log.warn("요청에 쿠키가 없습니다.");
        }

        // 토큰 유효성 검사 및 인증 객체 설정
        if (token != null && jwtUtil.validateToken(token)) {
            log.info("유효한 토큰입니다. SecurityContext 설정 시작");

            // 토큰으로부터 인증 객체 생성
            Authentication authentication = jwtUtil.getAuthentication(token);

            log.info("생성된 인증 객체: {}", authentication);

            // SecurityContext에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("유효하지 않은 토큰이거나 토큰이 없습니다.!!!!!!!");
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }

}

