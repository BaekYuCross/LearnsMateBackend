package intbyte4.learnsmate.preferred_topics.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindPreferredTopicsVO {

    private Long preferredTopicCode;
    private Long memberCode;
    private Integer lectureCategoryCode;
}
