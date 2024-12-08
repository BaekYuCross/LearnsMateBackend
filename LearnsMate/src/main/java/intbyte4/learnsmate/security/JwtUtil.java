package intbyte4.learnsmate.security;

import intbyte4.learnsmate.admin.domain.dto.JwtTokenDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
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
import java.time.*;
import java.time.Clock;
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
        this.expirationTime = expirationTime;
        this.adminService = adminService;
    }

    public boolean validateToken(String token) {
        try {
            log.info("Validating token: {}", token);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Expired Token: {}", token);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid Token: {}", token, e);
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

        if (userDetails instanceof CustomUserDetails) {
            Long expTimestamp = claims.get("exp", Long.class);
            LocalDateTime expirationDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(expTimestamp),
                    ZoneId.systemDefault()
            );
            ((CustomUserDetails) userDetails).setExpiration(expirationDateTime);
        }

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

    public String generateToken(JwtTokenDTO tokenDTO, List<String> roles, String provider, Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(tokenDTO.getUserCode());
        claims.put("email", tokenDTO.getUserEmail());
        claims.put("name", tokenDTO.getUserName());
        claims.put("roles", roles);
        claims.put("provider", provider);

        log.info("Claims 생성: {}", claims);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ZoneId kst = ZoneId.of("Asia/Seoul");
        ZonedDateTime now = ZonedDateTime.now(kst);
        ZonedDateTime expirationDateTime = now.plusHours(expirationTime / (1000 * 60 * 60));

        userDetails.setExpiration(expirationDateTime.toLocalDateTime());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expirationDateTime.toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(JwtTokenDTO tokenDTO) {
        Claims claims = Jwts.claims().setSubject(tokenDTO.getUserCode());
        log.info("RefreshToken Claims 생성: {}", claims);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime expirationDateTime = now.plusDays(7);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expirationDateTime.toInstant()))
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
        Date expiration = getClaimFromToken(token, Claims::getExpiration);
        ZonedDateTime utcExpiration = expiration.toInstant().atZone(ZoneId.of("UTC"));
        ZonedDateTime kstExpiration = utcExpiration.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return Date.from(kstExpiration.toInstant());
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
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", token, e);
            return null;
        }
    }

    public String extractUserCode(String expiredToken) {
        try {
            // 만료된 토큰에서도 클레임을 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(expiredToken)
                    .getBody();

            log.info("토큰이 유효하며, 클레임: {}", claims);

            // 클레임에서 subject (userCode) 반환
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 클레임을 추출 가능
            Claims claims = e.getClaims();

            log.info("토큰이 만료되었습니다. 만료 시각: {}", e.getClaims().getExpiration());
            String userCode = e.getClaims().getSubject();
            log.info("추출된 userCode: {}", userCode);

            return claims.getSubject(); // subject = userCode
        } catch (JwtException | IllegalArgumentException e) {
            log.error("토큰에서 사용자 코드를 추출하는 데 실패했습니다: {}", e.getMessage());
            throw new CommonException(StatusEnum.INVALID_TOKEN);
        }
    }

}
