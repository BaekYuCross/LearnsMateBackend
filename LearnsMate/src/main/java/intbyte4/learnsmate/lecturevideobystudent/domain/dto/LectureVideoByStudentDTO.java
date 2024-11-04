package intbyte4.learnsmate.lecturevideobystudent.domain.dto;

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
