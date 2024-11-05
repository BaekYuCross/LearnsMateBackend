package intbyte4.learnsmate.lecture.domain.dto;

import intbyte4.learnsmate.lecture.enums.LectureCategory;
import intbyte4.learnsmate.lecture.enums.LectureLevel;
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
    private LectureCategory lectureCategory;
    private LocalDateTime createdAt;
    private Integer contractStage;
    private LectureLevel lectureLevel;
    private Integer lecturePrice;
    private Boolean lectureStatus;
}
