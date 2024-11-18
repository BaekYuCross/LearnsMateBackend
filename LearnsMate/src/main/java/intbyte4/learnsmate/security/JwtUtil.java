package intbyte4.learnsmate.security;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.service.AdminService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    // JWT 토큰 서명 및 검증에 사용할 비밀 키
    private final Key secretKey;
    private long expirationTime;
    private AdminService adminService;

    // 생성자: 비밀키와 만료 시간 주입, BASE64로 인코딩된 비밀키를 디코딩하여 사용
    public JwtUtil(@Value("${token.secret}") String secretKey, @Value("${token.expiration_time}") long expirationTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // BASE64로 인코딩된 비밀키를 디코딩
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA 알고리즘을 사용하여 키 생성
        this.expirationTime = expirationTime; // JWT 만료 시간 설정
    }

    // 토큰이 유효한지 검사  Token 검증(Bearer 토큰이 넘어왔고, 우리 사이트의 secret key로 만들어 졌는가, 만료되었는지와 내용이 비어있진 않은지)
    public boolean validateToken(String token) {
        try {
            // JWT 토큰을 secretKey로 검증
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true; // 검증 성공
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e); // 토큰이 유효하지 않음
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token {}", e); // 토큰 만료됨
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e); // 지원하지 않는 토큰 형식
        } catch (IllegalArgumentException e) {
            log.info("JWT Token claims empty {}", e); // 토큰의 클레임이 비어 있음
        }

        return false; // 토큰이 유효하지 않음
    }

    // JWT 토큰에서 인증 정보를 추출하여 Authentication 객체 반환 ->넘어온 AccessToken으로 인증 객체 추출
    public Authentication getAuthentication(String token) {


        /* 설명. 토큰을 들고 왔던 들고 오지 않았던(로그인 시) 동일하게 security가 관리 할 UserDetails 타입을 정의 */
        UserDetails userDetails = adminService.loadUserByUsername(this.getUserId(token));

        // 토큰에서 클레임(Claims) 정보 추출
        Claims claims = parseClaims(token);
        log.info("넘어온 AccessToken claims 확인: {}", claims);

        // 권한 정보가 없는 경우 예외 처리
        Collection<? extends GrantedAuthority> authorities = null;
        if (claims.get("roles") == null) {
            log.warn("권한 정보가 없는 토큰입니다. Claims: {}", claims);
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        } else {
            // 클레임에서 권한 정보 추출
            authorities = Arrays.stream(claims.get("roles").toString()
                            .replace("[", "")
                            .replace("]", "")
                            .split(", "))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // 역할 앞에 "ROLE_" 접두사 추가
                    .collect(Collectors.toList());
        }

//        log.info("추출된 권한 정보: {}", authorities);
//
//        // 사용자 정보 추출
//        String userCode = claims.getSubject();
//        log.info("user code는 {}", userCode);
//        String email = claims.get("email", String.class);
//        String name = claims.get("name", String.class);
//
//        // CustomUserDetails 객체 생성
//        AdminDTO userDTO = new AdminDTO();
//        userDTO.setAdminCode(userDTO.getAdminCode());
//        userDTO.setAdminEmail(email);
//        userDTO.setAdminName(name);
//
//        // CustomUserDetails 객체로 인증 정보 생성
//        CustomUserDetails customUserDetails = new CustomUserDetails(userDTO, (List<GrantedAuthority>) authorities, true, true, true, true);

        // 인증 객체 생성하여 반환
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // Token에서 Claims 정보 추출
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    // Token에서 사용자 ID(주체 subject 클레임) 추출
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    // JWT 토큰 생성
    public String generateToken(JwtTokenDTO tokenDTO, List<String> roles, String provider) {
        // 클레임 정보 설정
        Claims claims = Jwts.claims().setSubject(tokenDTO.getUserCode());
        claims.put("email", tokenDTO.getUserEmail());
        claims.put("name", tokenDTO.getUserName());
        claims.put("roles", roles);
        claims.put("provider", "local");  // 항상 "local"로 설정

        log.info("claim의 모든 정보를 보자.: {}", claims.toString());

        // JWT 토큰 생성 및 서명
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간
                // 이 토큰이 언제 소멸될것인지 여기서 결정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // HMAC-SHA512 알고리즘으로 서명
                .compact(); // 토큰 생성
    }

    // 클레임에서 이메일 정보 추출
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }

    // 클레임에서 사용자 이름 정보 추출
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("name", String.class));
    }

    // 클레임에서 provider 정보 추출
    public String getProviderFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("provider", String.class));
    }

    // 토큰 만료 날짜 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 클레임에서 특정 정보를 추출하는 제네릭 메서드
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token); // 모든 클레임 추출
        return claimsResolver.apply(claims); // 특정 정보를 추출
    }

    // 토큰에서 모든 클레임 정보 추출
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰이 만료되었는지 여부 체크
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date()); // 만료된 경우 true 반환
    }

    // 토큰에서 사용자 코드(주체 클레임) 추출
    public String getUserCodeFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}
