package intbyte4.learnsmate.member.domain.vo;

import intbyte4.learnsmate.member.domain.MemberType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberFilterRequestVO {
    private Long memberCode;
    private MemberType memberType;
    private String memberEmail;
    private String memberName;
    private Integer memberAge;
    private String memberPhone;
    private String memberAddress;

    // 생년월일 범위
    private LocalDateTime birthStartDate;
    private LocalDateTime birthEndDate;

    // 생성일 범위
    private LocalDateTime createdStartDate;
    private LocalDateTime createdEndDate;

    // 수정일 범위
    private LocalDateTime updatedStartDate;
    private LocalDateTime updatedEndDate;
}
