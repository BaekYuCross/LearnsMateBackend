package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.admin.service.RedisService;
import intbyte4.learnsmate.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisService redisService;
    private final AdminService adminService;

    @Operation(summary = "직원 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /admin/logout 요청 도착");

        // 쿠키에서 refreshToken 추출
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 쿠키 삭제
        clearCookie(response, "token", "/", "localhost");
        clearCookie(response, "refreshToken", "/", "localhost");

        // Redis에서 refreshToken 삭제
        if (refreshToken != null && !refreshToken.isEmpty()) {
            // 토큰에서 userCode 추출
            String userCode = jwtUtil.getUserCodeFromToken(refreshToken); // 토큰에서 사용자 식별자 추출
            if (userCode != null) {
                redisService.deleteToken(userCode); // userCode 기반으로 Redis에서 삭제
            } else {
                log.warn("RefreshToken에서 userCode를 추출하지 못했습니다.");
            }
        }

        log.info("로그아웃 성공");
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    private void clearCookie(HttpServletResponse response, String cookieName, String path, String domain) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

    @Operation(summary = "토큰 리프레시 요청")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue("accessToken") String expiredToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 만료된 Access Token에서 사용자 ID 추출
            String userCode = jwtUtil.extractUserCode(expiredToken);

            // Redis에서 Refresh Token 확인
            String refreshToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
            if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
            }

            // UserDetails
            CustomUserDetails userDetails =
                    (CustomUserDetails) adminService.loadUserByUsername(userCode);

            // Authentication 객체
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 새로운 Access Token 발급
            String newAccessToken = jwtUtil.generateToken(
                    new JwtTokenDTO(userCode, null, null),
                    List.of("ROLE_USER"), null, authentication
            );

            // 새 Access Token을 HttpOnly 쿠키에 저장
            Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(15 * 60);
            response.addCookie(accessTokenCookie);

            return ResponseEntity.ok("새로운 Access Token 발급 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("재발급 실패: " + e.getMessage());
        }
    }

    //Postman으로 refreshToken값 조회
    @Operation(summary = "Redis에 담긴 refreshToken값 조회")
    @GetMapping("/check-refresh-token/{userCode}")
    public ResponseEntity<String> checkRefreshToken(@PathVariable String userCode) {
        String refreshToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
        if (refreshToken != null) {
            return ResponseEntity.ok(refreshToken);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Refresh Token not found");
    }
}

