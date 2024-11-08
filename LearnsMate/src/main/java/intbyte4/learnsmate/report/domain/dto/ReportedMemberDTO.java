package intbyte4.learnsmate.report.domain.dto;

import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportedMemberDTO {
    private Member reportedMember;
    private Integer reportCount;

    public ReportedMemberDTO(Member reportedMember, Long reportCount) {
        this.reportedMember = reportedMember;
        this.reportCount = reportCount != null ? reportCount.intValue() : 0; // Long to Integer 변환
    }
}
