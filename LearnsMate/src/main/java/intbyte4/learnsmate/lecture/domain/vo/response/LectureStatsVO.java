package intbyte4.learnsmate.lecture.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LectureStatsVO {
    private Integer totalStudentCount;
    private Integer totalLectureClickCount;
    private Double totalConversionRate;
    private String lectureCode;
    private String lectureTitle;
    private Integer studentCount;
    private Integer lectureClickCount;
    private Double conversionRate;
}
