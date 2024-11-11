package intbyte4.learnsmate.lecture_by_student.domain.dto;


import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureByStudentDTO {
    private Long lectureByStudentCode;
    private Boolean refundStatus;
    private Lecture lecture;
    private Member student;
}
