package intbyte4.learnsmate.comment.domain.vo.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindCommentVO {

    private Long commentCode;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberCode;
    private String lectureCode;
}
