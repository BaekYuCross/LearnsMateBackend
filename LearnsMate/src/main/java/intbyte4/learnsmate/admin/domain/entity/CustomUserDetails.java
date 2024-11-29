package intbyte4.learnsmate.admin.domain.entity;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

// security의 loadUserByUsername의 리턴 값을 바꿔주기 위한(토큰 생성을 위해) 커스텀 user
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {

    private final AdminDTO userDTO;   // Admin 엔티티를 담는 필드
    private List<GrantedAuthority> authorities;  // 권한 정보를 담을 필드
    private LocalDateTime expiration;

    // 추가 필드: true/false 값을 받을 필드
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Override
    public String getUsername() {
        return String.valueOf(userDTO.getAdminCode());  // 실제 유저의 사번을 반환
    }

    @Override
    public String getPassword() {
        return userDTO.getAdminPassword();  // 실제 비밀번호를 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;  // 사용자로부터 받은 값을 반환
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;  // 사용자로부터 받은 값을 반환
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;  // 사용자로부터 받은 값을 반환
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;  // 사용자로부터 받은 값을 반환
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 예시로 'ROLE_ADMIN'과 같은 권한을 반환
//        return List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));
//    }
}
