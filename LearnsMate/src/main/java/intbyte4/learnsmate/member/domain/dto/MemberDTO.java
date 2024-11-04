package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
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

    public MemberDTO insertMemberRequest(RequestSaveMemberVO request) {
        this.memberType = request.getMemberType();
        this.memberEmail = request.getMemberEmail();
        this.memberPassword = request.getMemberPassword();
        this.memberName = request.getMemberName();
        this.memberAge = request.getMemberAge();
        this.memberPhone = request.getMemberPhone();
        this.memberAddress = request.getMemberAddress();
        this.memberBirth = request.getMemberBirth();
        this.memberFlag = request.getMemberFlag();
        this.memberDormantStatus = request.getMemberDormantStatus();
        this.createdAt = request.getCreatedAt();
        this.updatedAt = request.getUpdatedAt();
        return this;
    }

    public Member toEntity(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return Member.builder()
                .memberType(this.memberType)
                .memberEmail(this.memberEmail)
                .memberPassword(this.memberPassword)
                .memberName(this.memberName)
                .memberAge(this.memberAge)
                .memberPhone(this.memberPhone)
                .memberAddress(this.memberAddress)
                .memberBirth(this.memberBirth)
                .memberFlag(this.memberFlag)
                .memberDormantStatus(this.memberDormantStatus)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
