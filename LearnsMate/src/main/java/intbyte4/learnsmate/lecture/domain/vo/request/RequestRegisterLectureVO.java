package intbyte4.learnsmate.lecture.domain.vo.request;

import intbyte4.learnsmate.lecture.enums.LectureCategoryEnum;
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
public class RequestRegisterLectureVO {
    private String lectureTitle;
    private LectureCategoryEnum lectureCategoryEnum;
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
