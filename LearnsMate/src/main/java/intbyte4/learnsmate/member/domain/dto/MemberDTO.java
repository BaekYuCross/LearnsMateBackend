package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.member.domain.MemberType;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO {
    private Long memberCode;
    private MemberType memberType;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private Integer memberAge;
    private String memberPhone;
    private String memberAddress;
    private LocalDateTime memberBirth;
    private Boolean memberFlag;
    private Boolean memberDormantStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
