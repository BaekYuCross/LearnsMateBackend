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

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    public ResponseEntity<?> logout(@RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        try {
            // 요청 본문에서 refreshToken 추출
            String refreshToken = requestBody.get("refreshToken");

            if (refreshToken == null || refreshToken.isEmpty()) {
                log.warn("RefreshToken is missing in the request body");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RefreshToken이 필요합니다.");
            }

            // refreshToken으로 사용자 정보 확인 및 Redis에서 토큰 삭제
            String userCode = jwtUtil.getUserCodeFromToken(refreshToken);
            redisService.deleteToken(userCode);

            // 응답에서 쿠키 제거
            clearCookie(response, "token");
            clearCookie(response, "refreshToken");

            log.info("User logged out successfully: {}", userCode);
            return ResponseEntity.ok().body("로그아웃 성공");
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "토큰 리프레시 요청")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        try {
            String refreshToken = requestBody.get("refreshToken");
            String userCode = jwtUtil.getUserCodeFromToken(refreshToken);

            if (userCode == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            String redisToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
            if (redisToken == null || !jwtUtil.validateToken(redisToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            CustomUserDetails userDetails = (CustomUserDetails) adminService.loadUserByUsername(userCode);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            String newAccessToken = jwtUtil.generateToken(new JwtTokenDTO(userCode, null, null), List.of("ROLE_USER"), null, authentication);

            ZonedDateTime kstExpiration = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(4);
            int[] expArray = new int[] {
                    kstExpiration.getYear(),
                    kstExpiration.getMonthValue(),
                    kstExpiration.getDayOfMonth(),
                    kstExpiration.getHour(),
                    kstExpiration.getMinute(),
                    kstExpiration.getSecond()
            };

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("accessToken", newAccessToken);
            responseBody.put("exp", expArray);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("Token refresh failed: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token refresh failed");
        }
    }

    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    //Postman으로 refreshToken값 조회
    @Operation(summary = "Redis에 담긴 refreshToken값 조회")
    @GetMapping("/auth/check-refresh-token/{userCode}")
    public ResponseEntity<String> checkRefreshToken(@PathVariable String userCode) {
        String refreshToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);
        return refreshToken != null ? ResponseEntity.ok(refreshToken)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
    }
}
