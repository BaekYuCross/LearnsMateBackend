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
import org.springframework.security.core.GrantedAuthority;
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
        String token = jwtUtil.generateToken(tokenDTO, roles, null);  // JWT 생성 (roles와 추가적인 데이터를 페이로드에 담음)

        // 생성된 JWT 토큰을 Authorization 헤더에 추가하여 응답 -> 생성된 JWT 토큰을 Authorization 헤더에 담아 응답으로 전송
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        log.info("JWT 생성 완료. Authorization 헤더에 추가됨.");
    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}