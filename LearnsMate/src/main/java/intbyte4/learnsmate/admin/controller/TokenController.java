package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.admin.service.RedisService;
import intbyte4.learnsmate.refresh_token.service.RefreshTokenService;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Operation(summary = "토큰 리프레시 요청")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키에서 refreshToken 추출
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    log.info("받은 refreshToken: {}", refreshToken);
                    break;
                }
            }
        }

        // 2. refreshToken 없으면 400
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refreshToken이 필요합니다.");
        }

        try {
            // 3. JWT 형식 확인
            if (!refreshToken.contains(".")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 refreshToken 형식입니다.");
            }

            // 4. 토큰에서 userCode 추출
            String userCode = jwtUtil.extractUserCode(refreshToken);

            // 5. Redis에서 refreshToken 확인
            String redisToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
            if (redisToken == null || !jwtUtil.validateToken(redisToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
            }

            // 6. 사용자 정보 로드 및 인증 객체 생성
            CustomUserDetails userDetails = (CustomUserDetails) adminService.loadUserByUsername(userCode);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 7. 새로운 Access Token 발급
            String newAccessToken = jwtUtil.generateToken(
                    new JwtTokenDTO(userCode, null, null),
                    List.of("ROLE_USER"), null, authentication
            );

            // 8. 만료 시간 계산
            Date expirationDate = jwtUtil.getExpirationDateFromToken(newAccessToken);

            ZonedDateTime kstExpiration = expirationDate.toInstant()
                    .atZone(ZoneId.of("Asia/Seoul"));

            String expirationTime = kstExpiration.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("새로운 토큰 만료 시간: {}", expirationTime);

            // 9. 쿠키에 Access Token 설정 (로컬 개발용)
//            Cookie accessTokenCookie = new Cookie("token", newAccessToken);
//            accessTokenCookie.setHttpOnly(true);
//            accessTokenCookie.setSecure(false); // 로컬에서는 HTTPS 안 쓰니까 false
//            accessTokenCookie.setPath("/");
//            accessTokenCookie.setMaxAge(15 * 60); // 15분
//
//            response.addCookie(accessTokenCookie);

            // Set-Cookie 수동 작성 (SameSite=Strict 포함)
            String accessTokenCookie = "token=" + newAccessToken +
                    "; Path=/" +
                    "; Max-Age=" + (15 * 60) +
                    "; HttpOnly" +
                    "; SameSite=Strict";

            response.setHeader("Set-Cookie", accessTokenCookie);

            // 10. 응답 바디에 만료 정보 포함
            return ResponseEntity.ok(Map.of(
                    "message", "새로운 Access Token 발급 완료!",
                    "exp", expirationTime
            ));

        } catch (Exception e) {
            log.error("토큰 재발급 실패", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("재발급 실패: " + e.getMessage());
        }
    }


    private void clearCookie(HttpServletResponse response, String cookieName, String path, String domain) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

//    private void clearCookie(HttpServletResponse response, String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//    }

    //Postman으로 refreshToken값 조회
    @Operation(summary = "Redis에 담긴 refreshToken값 조회")
    @GetMapping("/auth/check-refresh-token/{userCode}")
    public ResponseEntity<String> checkRefreshToken(@PathVariable String userCode) {
        String refreshToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
        return refreshToken != null ? ResponseEntity.ok(refreshToken)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
    }
}
