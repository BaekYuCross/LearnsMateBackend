package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.member.domain.MemberType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberFilterRequestDTO {
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
