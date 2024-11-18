package intbyte4.learnsmate.security;

import intbyte4.learnsmate.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
            = Arrays.asList("/users/verification-email/**"
            , "/users/nickname/check", "/users/verification-email/password", "/swagger-ui.html", "/swagger-ui/index.html"
            , "/users/password");

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("UsernamePasswordAuthenticationFilter보다 먼저 동작하는 필터");

        // Authorization 헤더에서 JWT 토큰을 추출
        String authorizationHeader = request.getHeader("Authorization");
        log.info("jwtFilter의 getHeader('Authorization'): {}", authorizationHeader);

        String token = null;

        // Authorization 헤더에 "Bearer "로 시작하는 토큰이 있으면 해당 토큰을 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
            log.info("Bearer 토큰 추출 완료: {}", token);
        } else {
            // 헤더에 토큰이 없으면 쿼리 파라미터에서 token 값을 찾음
            token = request.getParameter("token");
            log.info("OAuth 로그인: 쿼리 파라미터에서 토큰 추출. 토큰 : {}", token);
        }

        // 토큰이 있을 경우에만 유효성 검사 및 인증 처리 ->
        // 토큰이 유효하면 인증 정보를 SecurityContextHolder에 저장하고,
        // 이후의 필터가 인증 정보를 사용 가능하게 함.

        if (token != null && jwtUtil.validateToken(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);  // 토큰으로부터 인증 객체 추출
            log.info("JwtFilter를 통과한 유효한 토큰을 통해 security가 관리할 principal 객체: {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);   // 인증된 객체를 SecurityContext에 설정하여 이후의 요청에서 인증 정보를 사용할 수 있도록 함
        } else {
            log.warn("유효하지 않은 토큰이거나 토큰이 없습니다.");
        }

        // 인증 처리가 끝난 후, 다음 필터로 요청을 전달 -> 실행될 다음 필터는 UsernamePasswordAuthenticationFilter가 처리
        filterChain.doFilter(request, response);
    }
}
