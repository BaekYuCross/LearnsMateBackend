package intbyte4.learnsmate.member.domain.vo.response;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseFindMemberVO {
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
