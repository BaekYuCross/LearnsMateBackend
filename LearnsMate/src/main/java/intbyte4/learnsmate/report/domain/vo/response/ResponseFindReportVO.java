package intbyte4.learnsmate.report.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindReportVO {
    private Long reportCode;
    private String reportReason;
    private LocalDateTime reportDate;
    private Long commentCode;
    private Long reportMemberCode;
    private Long reportedMemberCode;
}
