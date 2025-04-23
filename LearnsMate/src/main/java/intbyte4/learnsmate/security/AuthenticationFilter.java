package intbyte4.learnsmate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.RequestLoginVO;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.refresh_token.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private AdminService userService;
    private Environment env;
    private JwtUtil jwtUtil;
    private RedisTemplate<String, String> redisTemplate;
    private RefreshTokenService refreshTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AdminService userService, Environment env, JwtUtil jwtUtil,
                                RedisTemplate<String, String> redisTemplate, RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.refreshTokenService = refreshTokenService;

        // 커스텀 로그인 경로 설정
        setFilterProcessesUrl("/users/login");
    }


    // 로그인 시도 시 동작 "/users/login" 요청 시.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // InputStream이 비어있는지 확인
            if (request.getInputStream().available() == 0) {
                log.warn("Request InputStream is empty");
                throw new RuntimeException("Request body is empty. Login data is required.");
            }

            // JSON 데이터를 RequestLoginVO로 매핑
            RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);

            log.info("Parsed Login Data: adminCode={}, adminPassword=****", creds.getAdminCode());

            // 인증 매니저에 인증 요청
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getAdminCode(), // 사용자 ID
                            creds.getAdminPassword(), // 사용자 비밀번호
                            new ArrayList<>()
                    ));
        } catch (IOException e) {
            log.error("Error reading request InputStream or mapping to RequestLoginVO: ", e);
            throw new RuntimeException("Failed to parse login request data", e);
        }
    }


    // 로그인 성공 시 실행되는 메소드 -> 여기서 JWT를 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // 로그인 성공 후 security가 관리하는 principal 객체를 로그로 출력
        log.info("로그인 성공하고 security가 관리하는 principal 객체(authResult): {}", authResult);

        // Principal 객체 확인 및 캐스팅
        if (!(authResult.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalArgumentException("Authentication 객체가 CustomUserDetails 타입이 아닙니다.");
        }

        // CustomUserDetails로 캐스팅하여 사용자 정보를 가져옴
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        // 사용자 정보 가져오기
        String userCode = userDetails.getUsername(); // username이 userCode로 설정
        String userEmail = userDetails.getUserDTO().getAdminEmail(); // 이메일
        String userName = userDetails.getUserDTO().getAdminName(); // 이름

        log.info("인증된 사용자 정보 - userCode: {}, email: {}, userName: {}", userCode, userEmail, userName);

        // 인증된 사용자의 권한을 가져와 List<String>으로 변환
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("roles: {}", roles.toString());  // 권한 목록 출력

        // JWT 생성: JwtTokenDTO에 사용자 정보를 담고, roles와 함께 JWT 토큰을 생성
        JwtTokenDTO tokenDTO = new JwtTokenDTO(userCode, userEmail, userName);
        String token = jwtUtil.generateToken(tokenDTO, roles, null, authResult);  // JWT 생성 (roles와 추가적인 데이터를 페이로드에 담음)
        String refreshToken = jwtUtil.generateRefreshToken(tokenDTO); // 7일

        // Access Token Set-Cookie 수동 구성 (운영 환경)
//        String accessTokenCookie = "token=" + token +
//                "; Path=/" +
//                "; Max-Age=" + (4 * 3600) + // 4시간
//                "; HttpOnly" +
//                "; Secure" + // HTTPS 환경에서만 전송
//                "; SameSite=None"; // 크로스 도메인 허용 시 필요
//
//        // Refresh Token Set-Cookie 수동 구성 (운영 환경)
//        String refreshTokenCookie = "refreshToken=" + refreshToken +
//                "; Path=/" +
//                "; Max-Age=" + (7 * 24 * 3600) + // 7일
//                "; HttpOnly" +
//                "; Secure" + // HTTPS 필수
//                "; SameSite=None"; // 프론트와 백 도메인이 다를 경우 필수
//
        // Access Token Set-Cookie 수동 구성
        String accessTokenCookie = "token=" + token +
                "; Path=/" +
                "; Max-Age=" + (4 * 3600) +
                "; HttpOnly" +
                "; SameSite=Strict"; // ← 핵심 포인트

        // Refresh Token Set-Cookie 수동 구성
        String refreshTokenCookie = "refreshToken=" + refreshToken +
                "; Path=/" +
                "; Max-Age=" + (7 * 24 * 3600) +
                "; HttpOnly" +
                "; SameSite=Strict";

        // 쿠키 헤더로 추가
        response.addHeader("Set-Cookie", accessTokenCookie);
        response.addHeader("Set-Cookie", refreshTokenCookie);

        // Redis 저장
        saveRefreshTokenToRedis(userCode, refreshToken);

        log.info("Access Token 및 Refresh Token 생성 완료 !!!!!!!!!!!!!!!!!!!!");

        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        ZonedDateTime kstExpiration = ZonedDateTime.ofInstant(expirationDate.toInstant(), ZoneId.of("Asia/Seoul"));

        // KST 시간을 배열 형태로 전달
        int[] expArray = new int[] {
                kstExpiration.getYear(),
                kstExpiration.getMonthValue(),
                kstExpiration.getDayOfMonth(),
                kstExpiration.getHour(),
                kstExpiration.getMinute(),
                kstExpiration.getSecond()
        };

        // JSON으로 사용자 데이터 반환
        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("accessToken", token);
//        responseData.put("refreshToken", refreshToken);
        responseData.put("exp", expArray);
        responseData.put("name", userName);
        responseData.put("code", userCode);
        responseData.put("adminDepartment", userDetails.getUserDTO().getAdminDepartment());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
    }


    // Redis에 refreshToken 저장
    public void saveRefreshTokenToRedis(String userCode, String refreshToken) {
        try {
            log.info("Saved Refresh Token to Redis: Key=refreshToken:{}, Value={}", userCode, refreshToken);

            // Redis에 refreshToken 저장
            redisTemplate.opsForValue().set(
                    "refreshToken:" + userCode,
                    refreshToken,
                    7, // 7일
                    TimeUnit.DAYS
            );
        } catch (Exception e) {
            log.error("Redis save fail - saveRefreshTokenToRedis : ", e);
        }
    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}