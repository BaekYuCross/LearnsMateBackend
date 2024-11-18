package intbyte4.learnsmate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.RequestLoginVO;
import intbyte4.learnsmate.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private AdminService userService;
    private Environment env;
    private JwtUtil jwtUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AdminService userService, Environment env, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.jwtUtil = jwtUtil;

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
                    new UsernamePasswordAuthenticationToken(creds.getUserCode(), creds.getUserPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 시 실행되는 메소드 -> 여기서 JWT를 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // 로그인 성공 후 security가 관리하는 principal 객체를 로그로 출력
        log.info("로그인 성공하고 security가 관리하는 principal 객체(authResult): {}", authResult);

        // 로그인 후 인증된 Authentication 객체를 사용하여 JWT 토큰 생성 준비 -> JWT 생성 시 사용할 시크릿 키를 로그로 출력
        log.info("시크릿 키: " + env.getProperty("token.secret"));

        // JWT의 payload에 담을 정보들을 수집 (id, 권한들, 만료 시간 등) -> 인증된 사용자의 관리자 코드(사번 등) 가져오기
        Long adminCode = ((Admin)authResult.getPrincipal()).getAdminCode();

        // CustomUserDetails로 인증 객체를 캐스팅하여 사용자 정보를 가져옴
        CustomUserDetails userDetails = null;

        if (authResult.getPrincipal() instanceof CustomUserDetails) {  // 인증 객체가 CustomUserDetails인 경우
            userDetails = (CustomUserDetails) authResult.getPrincipal();  // 인증 객체를 CustomUserDetails로 캐스팅
            log.info("userDetails: {}", userDetails);  // userDetails 정보 출력
            log.info("Authentication: {}", authResult);  // Authentication 객체 출력
            // 이후 작업 계속
        } else {
            // 인증 객체가 CustomUserDetails가 아닐 경우 예외 처리
            throw new IllegalArgumentException("인증 객체가 CustomUserDetails가 아닙니다.");
        }

        // 사용자 정보를 JWT에 담기 위한 준비
        String userCode = userDetails.getUsername(); // 사번 (username으로 저장된 사번)
        String userEmail = userDetails.getUserDTO().getAdminEmail(); // 이메일
        String userName = userDetails.getUserDTO().getAdminName(); // 사용자 이름

        // 로그로 인증된 회원 정보 출력
        log.info("인증된 회원의 userCode: " + userCode);
        log.info("인증된 회원의 email: " + userEmail);
        log.info("인증된 회원의 userNickname: " + userName);

        // 인증된 사용자의 권한을 가져와 List<String>으로 변환
        List<String> roles = authResult.getAuthorities().stream()  // 권한 리스트를 스트림으로 처리
                .map(role -> role.getAuthority())  // 권한의 이름을 가져옴
                .collect(Collectors.toList());  // List로 변환
        log.info("roles: {}", roles.toString());  // 권한 목록 출력

        // JWT 생성: JwtTokenDTO에 사용자 정보를 담고, roles와 함께 JWT 토큰을 생성
        JwtTokenDTO tokenDTO = new JwtTokenDTO(userCode, userEmail, userName);  // JwtTokenDTO에 사번, 이메일, 이름을 담음
        String token = jwtUtil.generateToken(tokenDTO, roles, null);  // JWT 생성 (roles와 추가적인 데이터를 페이로드에 담음)

        // 생성된 JWT 토큰을 Authorization 헤더에 추가하여 응답 -> 생성된 JWT 토큰을 Authorization 헤더에 담아 응답으로 전송
        response.addHeader(HttpHeaders.AUTHORIZATION, token);
    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}