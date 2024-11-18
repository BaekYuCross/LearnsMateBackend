package intbyte4.learnsmate.preferred_topics.domain.dto;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PreferredTopicStatsDTO {
    private Integer year; // YEAR(m.createdAt)
    private Integer month; // MONTH(m.createdAt)
    private LectureCategoryEnum lectureCategoryName; // lc.lectureCategoryName
    private Long topicCount; // COUNT(pt.preferredTopicCode)
}