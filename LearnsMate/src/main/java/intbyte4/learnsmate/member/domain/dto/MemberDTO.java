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

    public static Member toEntity(MemberDTO dto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return Member.builder()
                .memberType(dto.getMemberType())
                .memberEmail(dto.getMemberEmail())
                .memberPassword(dto.getMemberPassword())
                .memberName(dto.getMemberName())
                .memberAge(dto.getMemberAge())
                .memberPhone(dto.getMemberPhone())
                .memberAddress(dto.getMemberAddress())
                .memberBirth(dto.getMemberBirth())
                .memberFlag(dto.getMemberFlag())
                .memberDormantStatus(dto.getMemberDormantStatus())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
