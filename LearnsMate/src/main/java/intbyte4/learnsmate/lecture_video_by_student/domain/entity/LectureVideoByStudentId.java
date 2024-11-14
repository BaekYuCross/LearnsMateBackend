package intbyte4.learnsmate.lecture_video_by_student.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LectureVideoByStudentId implements Serializable {
    private Long videoCode;
    private Long lectureByStudentCode;
}
