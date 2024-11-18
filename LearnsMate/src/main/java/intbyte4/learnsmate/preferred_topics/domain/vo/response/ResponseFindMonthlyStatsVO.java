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
public class ResponseFindMonthlyStatsVO {

    private String yearMonth; // "YYYY-MM" 형식의 월 정보
    private String lectureCategoryName;  // 카테고리 이름
    private Long topicCount;  // 카테고리별 개수
}
