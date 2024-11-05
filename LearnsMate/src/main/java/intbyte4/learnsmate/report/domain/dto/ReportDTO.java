package intbyte4.learnsmate.report.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {
    private Long reportCode;
    private String reportReason;
    private LocalDateTime reportDate;
    private Long commentCode;
    private Long reportMemberCode;
    private Long reportedMemberCode;
}
