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
        try {
            // 로그로 쿠키 출력
            Cookie[] cookies = request.getCookies();
            log.info("Received cookies: {}", Arrays.toString(cookies));

            String refreshToken = Arrays.stream(Optional.ofNullable(cookies).orElse(new Cookie[] {}))
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);

            if (refreshToken == null || refreshToken.isEmpty()) {
                log.warn("RefreshToken is missing in the request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RefreshToken이 필요합니다.");
            }

            String userCode = jwtUtil.getUserCodeFromToken(refreshToken);
            redisService.deleteToken(userCode);

            clearCookie(response, "token");
            clearCookie(response, "refreshToken");

            return ResponseEntity.ok().body("로그아웃 성공");
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }


    @Operation(summary = "토큰 리프레시 요청")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[] {}))
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refreshToken이 필요합니다.");
            }

            String userCode = jwtUtil.getUserCodeFromToken(refreshToken);
            String redisToken = redisTemplate.opsForValue().get("refreshToken:" + userCode);

            if (redisToken == null || !jwtUtil.validateToken(redisToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
            }

            CustomUserDetails userDetails = (CustomUserDetails) adminService.loadUserByUsername(userCode);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            String newAccessToken = jwtUtil.generateToken(new JwtTokenDTO(userCode, null, null), List.of("ROLE_USER"), null, authentication);

            response.addCookie(createCookie("token", newAccessToken, 15 * 60)); // 15분

            return ResponseEntity.ok(Map.of("message", "새로운 Access Token 발급 완료 !"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 재발급 중 오류 발생");
        }
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
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
