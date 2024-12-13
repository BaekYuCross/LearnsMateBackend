package intbyte4.learnsmate.lecture.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LectureStatsFilterDTO {
    private Integer startYear;
    private Integer startMonth;
    private Integer endYear;
    private Integer endMonth;
}