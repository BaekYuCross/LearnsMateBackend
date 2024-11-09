package intbyte4.learnsmate.blacklist.domain.vo.response;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseFindReservedBlacklistOneVO {

    private ReportDTO reportDTO;
    private CommentDTO commentDTO;
}
