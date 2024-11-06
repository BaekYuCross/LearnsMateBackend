package intbyte4.learnsmate.lecture.domain.vo;

import intbyte4.learnsmate.lecture.enums.LectureCategory;
import intbyte4.learnsmate.lecture.enums.LectureLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EditLectureInfoVO {
    private String lectureTitle;
    private LectureCategory lectureCategory;
    private Boolean lectureConfirmStatus;
    private LocalDateTime updatedAt;
    private String lectureImage;
    private Integer lecturePrice;
    private Boolean lectureStatus;
    private Integer lectureClickCount;
    private LectureLevel lectureLevel;
}
