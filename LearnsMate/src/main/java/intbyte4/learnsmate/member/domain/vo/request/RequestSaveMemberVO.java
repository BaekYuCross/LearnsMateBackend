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

    public static MemberDTO toDTO(RequestSaveMemberVO request) {
        return MemberDTO.builder()
                .memberCode(null) // 새로 생성되는 회원이므로 null로 설정
                .memberType(request.getMemberType())
                .memberEmail(request.getMemberEmail())
                .memberPassword(request.getMemberPassword())
                .memberName(request.getMemberName())
                .memberAge(request.getMemberAge())
                .memberPhone(request.getMemberPhone())
                .memberAddress(request.getMemberAddress())
                .memberBirth(request.getMemberBirth())
                .memberFlag(true)
                .memberDormantStatus(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()) // 생성 시점 설정
                .build();
    }
}
