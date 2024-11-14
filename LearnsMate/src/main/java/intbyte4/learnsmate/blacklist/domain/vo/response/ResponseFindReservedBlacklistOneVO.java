package intbyte4.learnsmate.blacklist.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindReservedBlacklistOneVO {

    private ReportDTO reportDTO;
    private CommentDTO commentDTO;
}
