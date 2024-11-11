package intbyte4.learnsmate.login_history.domain.dto;

import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LoginHistoryDTO {
    private Long loginHistoryCode;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastLogoutDate;
    private Long memberCode;
}
