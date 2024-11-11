package intbyte4.learnsmate.lecture_category_by_lecture.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindLectureCategoryByLectureVO {
    private Long lectureCategoryByLectureCode;
    private Long lectureCode;
    private Integer lectureCategoryCode;
}
