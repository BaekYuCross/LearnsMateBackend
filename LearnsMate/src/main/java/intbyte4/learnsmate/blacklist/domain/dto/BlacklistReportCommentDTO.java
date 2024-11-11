package intbyte4.learnsmate.blacklist.domain.dto;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlacklistReportCommentDTO {
    private ReportDTO reportDTO;
    private CommentDTO commentDTO;
}
