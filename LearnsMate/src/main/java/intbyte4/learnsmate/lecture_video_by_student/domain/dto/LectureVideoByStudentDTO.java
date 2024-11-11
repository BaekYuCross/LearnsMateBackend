package intbyte4.learnsmate.lecture_video_by_student.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureVideoByStudentDTO {
    private Long videoCode;
    private Long lectureByStudentCode;
    private Boolean lectureStatus;
}
