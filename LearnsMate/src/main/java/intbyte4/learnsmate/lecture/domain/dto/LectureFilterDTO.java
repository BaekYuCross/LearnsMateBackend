package intbyte4.learnsmate.lecture.domain.dto;

import intbyte4.learnsmate.lecture.enums.LectureCategoryEnum;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LectureFilterDTO {
    private Long lectureCode;
    private String lectureTitle;
    private Long tutorCode;
    private String tutorName;
    private LectureCategoryEnum lectureCategoryEnum;
    private LocalDateTime createdAt;
    private Integer contractStage;
    private LectureLevelEnum lectureLevel;
    private Integer lecturePrice;
    private Boolean lectureStatus;
}
