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

    // MemberDTO를 받아서 VO 필드를 초기화하는 생성자
    public ResponseFindMemberVO(MemberDTO dto) {
        this.memberCode = dto.getMemberCode();
        this.memberType = dto.getMemberType();
        this.memberEmail = dto.getMemberEmail();
        this.memberName = dto.getMemberName();
        this.memberAge = dto.getMemberAge();
        this.memberPhone = dto.getMemberPhone();
        this.memberAddress = dto.getMemberAddress();
    }
}
