package intbyte4.learnsmate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.RequestLoginVO;
import intbyte4.learnsmate.admin.service.AdminService;
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

    public AuthenticationFilter(AuthenticationManager authenticationManager, AdminService userService, Environment env, JwtUtil jwtUtil,
                                RedisTemplate<String, String> redisTemplate) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;

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
        log.info("인증 성공 - 시작");

        // Principal 객체 확인 및 캐스팅
        if (!(authResult.getPrincipal() instanceof CustomUserDetails)) {
            log.error("Authentication 객체가 CustomUserDetails 타입이 아닙니다.");
            throw new IllegalArgumentException("Authentication 객체가 CustomUserDetails 타입이 아닙니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        log.info("사용자 정보 가져옴: {}", userDetails.getUsername());

        String userCode = userDetails.getUsername();
        String userEmail = userDetails.getUserDTO().getAdminEmail();
        String userName = userDetails.getUserDTO().getAdminName();
        log.info("사용자 상세 정보 추출 완료: code={}, email={}, name={}", userCode, userEmail, userName);

        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // JWT 생성
        JwtTokenDTO tokenDTO = new JwtTokenDTO(userCode, userEmail, userName);
        String token = jwtUtil.generateToken(tokenDTO, roles, null, authResult);

        // 만료 시간 계산 및 추가
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        String expTime = expirationDate.toInstant().toString();

        String refreshToken = jwtUtil.generateRefreshToken(tokenDTO);
        log.info("토큰 생성 완료");

        try {
            saveRefreshTokenToRedis(userCode, refreshToken);
            log.info("Redis에 Refresh Token 저장 완료");
        } catch (Exception e) {
            log.error("Redis 저장 실패: {}", e.getMessage(), e);
        }

        log.info("Generated exp for frontend: {}", expTime);

        // JSON으로 토큰 반환
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", token);
        responseData.put("refreshToken", refreshToken);
        responseData.put("exp", expTime);
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