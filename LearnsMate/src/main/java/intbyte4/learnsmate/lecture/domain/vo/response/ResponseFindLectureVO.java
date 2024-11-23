package intbyte4.learnsmate.lecture.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindLectureVO {
    private String lectureCode;
    private String lectureTitle;
    private Long tutorCode;
    private String tutorName;
    private String lectureCategoryName;
    private LectureLevelEnum lectureLevel;
    private LocalDateTime createdAt;
    private Integer lecturePrice;
    private Boolean lectureConfirmStatus;
    private Boolean lectureStatus;
}
