package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

            // 새로운 Access Token 발급
            String newAccessToken = jwtUtil.generateToken(
                    new JwtTokenDTO(userCode, null, null),
                    List.of("ROLE_USER"),null
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


}

