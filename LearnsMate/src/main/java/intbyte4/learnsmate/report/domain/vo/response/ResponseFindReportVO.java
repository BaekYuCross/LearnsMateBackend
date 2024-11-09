package intbyte4.learnsmate.report.domain.vo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFindReportVO {
    private Long reportCode;
    private String reportReason;
    private LocalDateTime reportDate;
    private Long commentCode;
    private Long reportMemberCode;
    private Long reportedMemberCode;
}
