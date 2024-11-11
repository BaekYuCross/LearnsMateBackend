package intbyte4.learnsmate.lecture.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseRegisterLectureVO {
    private Long lectureCode;
    private String lectureTitle;
    private Boolean lectureConfirmStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lectureImage;
    private Integer lecturePrice;
    private Long tutorCode;
    private Boolean lectureStatus;
    private Integer lectureClickCount;
    private LectureLevelEnum lectureLevel;
}
