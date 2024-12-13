package intbyte4.learnsmate.preferred_topics.domain.dto;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PreferredTopicStatsConvertDTO {
    private String yearMonth;
    private String lectureCategoryName;
    private Long topicCount;
}
