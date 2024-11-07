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
}
