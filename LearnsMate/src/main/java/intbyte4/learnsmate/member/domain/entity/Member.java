package intbyte4.learnsmate.member.domain.entity;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "member")
@Table(name = "members")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Member {

    @Id
    @Column(name = "member_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_age", nullable = false)
    private Integer memberAge;

    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    @Column(name = "member_address", nullable = false)
    private String memberAddress;

    @Column(name = "member_birth", nullable = false)
    private LocalDateTime memberBirth;

    @Column(name = "member_flag", nullable = false)
    private Boolean memberFlag;

    @Column(name = "member_dormant_status", nullable = false)
    private Boolean memberDormantStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void deactivate() {
        this.memberFlag = false;
    }
}
