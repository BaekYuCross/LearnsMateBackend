package intbyte4.learnsmate.security;

import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key secretKey;
    private final long expirationTime;
    private final AdminService adminService;

    public JwtUtil(@Value("${token.secret}") String secretKey, @Value("${token.expiration_time}") long expirationTime, AdminService adminService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime * 1000L;
        this.adminService = adminService;
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                log.warn("토큰이 null이거나 빈 문자열입니다.");
                return false;
            }
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("유효하지 않은 JWT 서명입니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT 클레임이 비어있거나 잘못되었습니다: {}", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        UserDetails userDetails = adminService.loadUserByUsername(this.getUserId(token));
        Claims claims = parseClaims(token);
        log.info("AccessToken Claims: {}", claims);

        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        if (claims.get("roles") == null || claims.get("roles").toString().isEmpty()) {
            log.warn("권한 정보가 없는 토큰입니다. Claims: {}", claims);
            throw new CommonException(StatusEnum.RESTRICTED);
        }
        return Arrays.stream(claims.get("roles").toString()
                        .replace("[", "").replace("]", "").split(", "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public String generateToken(JwtTokenDTO tokenDTO, List<String> roles, String provider) {
        Claims claims = Jwts.claims().setSubject(tokenDTO.getUserCode());
        claims.put("email", tokenDTO.getUserEmail());
        claims.put("name", tokenDTO.getUserName());
        claims.put("roles", roles);
        claims.put("provider", provider);

        log.info("Claims 생성: {}", claims);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 리프레시 토큰은 단순히 새로운 액세스 토큰 발급을 요청하기 위한 용도
    public String generateRefreshToken(JwtTokenDTO tokenDTO) {
        Claims claims = Jwts.claims().setSubject(tokenDTO.getUserCode()); // 최소한의 정보
        log.info("RefreshToken Claims 생성: {}", claims);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7일 만료
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("name", String.class));
    }

    public String getProviderFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("provider", String.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUserCodeFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }



}
