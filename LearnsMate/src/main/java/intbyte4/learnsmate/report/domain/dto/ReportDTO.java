package intbyte4.learnsmate.report.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reportDate;
    private Long commentCode;
    private Long reportMemberCode;
    private Long reportedMemberCode;
}
