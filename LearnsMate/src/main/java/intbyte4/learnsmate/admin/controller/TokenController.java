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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        try {
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                    }
                }
            }
            log.info("Extracted Refresh Token: {}", refreshToken);

            clearCookie(response, "token", "/", "learnsmate.shop");
            clearCookie(response, "refreshToken", "/", "learnsmate.shop");

            if (refreshToken != null && !refreshToken.isEmpty()) {
                String userCode = jwtUtil.getUserCodeFromToken(refreshToken);
                log.info("Extracted userCode from Token: {}", userCode);
                if (userCode != null) {
                    redisService.deleteToken(userCode);
                } else {
                    log.warn("RefreshToken에서 userCode를 추출하지 못했습니다.");
                }
            }

            return ResponseEntity.ok().body("로그아웃 성공");
        } catch (Exception e) {
            log.error("An error occurred during logout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패");
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

    @Operation(summary = "토큰 리프레시 요청")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 refreshToken을 추출
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();  // refreshToken 값 저장
                }
            }
        }

        // refreshToken이 없으면 400 에러 반환
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refreshToken이 필요합니다.");
        }

        try {
            // JWT 토큰 형식 검증
            if (!refreshToken.contains(".")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 refreshToken 형식입니다.");
            }

            // 만료된 Access Token에서 사용자 ID 추출
            String userCode = jwtUtil.extractUserCode(refreshToken);

            // Redis에서 Refresh Token 확인
            String redisToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
            if (redisToken == null || !jwtUtil.validateToken(redisToken)) {
                throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
            }

            // UserDetails 조회 (CustomUserDetails는 사용자 정보를 가져오는 클래스)
            CustomUserDetails userDetails = (CustomUserDetails) adminService.loadUserByUsername(userCode);

            // Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 새로운 Access Token 발급
            String newAccessToken = jwtUtil.generateToken(
                    new JwtTokenDTO(userCode, null, null),
                    List.of("ROLE_USER"), null, authentication
            );

            // 토큰 만료 시간 추출 (JWT의 exp 클레임에서)
            Date expirationDate = jwtUtil.getExpirationDateFromToken(newAccessToken);

            // 만료 시간을 문자열로 포맷
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String expirationTime = dateFormat.format(expirationDate);

            // 쿠키에 새로운 Access Token 설정
            Cookie accessTokenCookie = new Cookie("token", newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);  // 로컬 개발 환경에서는 false로 설정
            accessTokenCookie.setPath("/");  // 모든 경로에서 쿠키 사용 가능
            accessTokenCookie.setMaxAge(15 * 60);  // 15분

            // SameSite=None 속성 추가 (CORS 문제 해결)
            accessTokenCookie.setDomain("learnsmate.shop");  // 도메인 설정 (로컬 개발 환경에서는 localhost)
            accessTokenCookie.setSecure(true);  // HTTPS에서만 전송되도록 설정

            // 응답에 쿠키 추가
            response.addCookie(accessTokenCookie);

            // Set-Cookie 헤더로 새로운 토큰 추가
            response.setHeader("Set-Cookie", "token=" + newAccessToken +
                    "; HttpOnly; Secure; Path=/; Max-Age=" + (15 * 60) + "; SameSite=None");

            return ResponseEntity.ok(Map.of("message", "새로운 Access Token 발급 완료 !", "exp", expirationTime));
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

