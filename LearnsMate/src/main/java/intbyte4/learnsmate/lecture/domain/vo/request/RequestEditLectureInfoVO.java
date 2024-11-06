package intbyte4.learnsmate.lecture.domain.vo.request;

import intbyte4.learnsmate.lecture.enums.LectureCategoryEnum;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestEditLectureInfoVO {
    private String lectureTitle;
    private LectureCategoryEnum lectureCategoryEnum;
    private Boolean lectureConfirmStatus;
    private LocalDateTime updatedAt;
    private String lectureImage;
    private Integer lecturePrice;
    private Boolean lectureStatus;
    private Integer lectureClickCount;
    private LectureLevelEnum lectureLevel;
}
