package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.member.domain.MemberType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberFilterRequestDTO {

    private Long memberCode;
    private String memberName;
    private MemberType memberType;
    private String memberEmail;
    private String memberPhone;
    private String memberAddress;

    // 나이 범위
    private Integer memberStartAge;
    private Integer memberEndAge;

    // 생년월일 범위
    private LocalDateTime birthStartDate;
    private LocalDateTime birthEndDate;

    private Boolean memberFlag;
    private Boolean memberDormantFlag;

    // 생성일 범위
    private LocalDateTime createdStartDate;
    private LocalDateTime createdEndDate;

    private List<String> selectedColumns;
}
