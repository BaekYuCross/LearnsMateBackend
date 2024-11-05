package intbyte4.learnsmate.member.domain.vo.request;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestSaveMemberVO {
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

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                .memberCode(null) // 새로 생성되는 회원이므로 null로 설정
                .memberType(this.memberType) // 인스턴스의 필드를 사용
                .memberEmail(this.memberEmail)
                .memberPassword(this.memberPassword)
                .memberName(this.memberName)
                .memberAge(this.memberAge)
                .memberPhone(this.memberPhone)
                .memberAddress(this.memberAddress)
                .memberBirth(this.memberBirth)
                .memberFlag(true) // 기본값 설정
                .memberDormantStatus(false) // 기본값 설정
                .createdAt(LocalDateTime.now()) // 생성 시점 설정
                .updatedAt(LocalDateTime.now()) // 생성 시점 설정
                .build();
    }
}
