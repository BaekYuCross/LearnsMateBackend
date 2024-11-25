package intbyte4.learnsmate.login_history.domain.entity;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "loginHistory")
@Table(name = "login_history")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginHistory {

    @Id
    @Column(name = "login_history_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginHistoryCode;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "last_logout_date")
    private LocalDateTime lastLogoutDate;

    @ManyToOne
    @JoinColumn(name = "member_code", nullable = false)
    private Member member;

    public void updateLogoutDate(){
        this.lastLogoutDate = LocalDateTime.now();
        if(lastLogoutDate.isBefore(lastLoginDate)){
            throw new CommonException(StatusEnum.DATA_INTEGRITY_VIOLATION);
        }
    }
}
