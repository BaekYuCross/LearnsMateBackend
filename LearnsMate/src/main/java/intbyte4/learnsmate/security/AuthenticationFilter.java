package intbyte4.learnsmate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.RequestLoginVO;
import intbyte4.learnsmate.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
;

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
        // 응답과 요청의값을 인자로 받아주고, 내부의 유저 정보를 가져오자
        log.info("로그인시 동작하는 기능임. authenticationfilter에요");

        // request body에 담긴 내용을 우리가 만든 RequestLoginVO 타입에 담는다.(일종의 @RequestBody의 개념)
        try {
            RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                             creds.getAdminCode(), // 사용자 사번
                            creds.getAdminPassword(), // 사용자 비밀번호
                            new ArrayList<>()
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        String accessToken = jwtUtil.generateToken(tokenDTO, roles, null);  // JWT 생성 (roles와 추가적인 데이터를 페이로드에 담음)
        String refreshToken = jwtUtil.generateRefreshToken(tokenDTO); // 7일

        // 쿠키 생성
        Cookie jwtCookie = new Cookie("accessToken", accessToken);
        jwtCookie.setHttpOnly(true); // HTTP Only 속성으로 설정 (JavaScript에서 접근 불가)
        jwtCookie.setSecure(false); // HTTPS 연결에서만 전송 (테스트 환경에서는 false 설정 가능)
        // https://learnsmate.site -> 배포 환경시 true로 전환
        jwtCookie.setPath("/"); // 쿠키의 유효 경로 설정 (애플리케이션 전체에 사용 가능)
        jwtCookie.setMaxAge(4 * 3600); // 쿠키 만료 시간 설정 (4시간)
        // 여기를 3-4시간정도로 만료시간 할건데 리프레시토큰을 해야하나? erp라 재로그인이 필요하지않을까


        // Refresh Token 쿠키 생성
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);  // 개발 환경에서 false, 배포 환경에서는 true로 설정
        refreshTokenCookie.setPath("/"); // 유효 경로 설정
        refreshTokenCookie.setMaxAge(7 * 24 * 3600); // Refresh Token의 만료 시간 (7일)

        // 쿠키 응답에 추가
        response.addCookie(jwtCookie);
        response.addCookie(refreshTokenCookie);

        saveRefreshTokenToRedis(userCode,refreshToken);

        log.info("Access Token 및 Refresh Token 생성 완료 !!!!!!!!!!!!!!!!!!!!");
    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}