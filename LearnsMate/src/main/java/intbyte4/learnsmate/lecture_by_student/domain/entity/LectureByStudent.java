package intbyte4.learnsmate.lecture_by_student.domain.entity;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;


@Entity(name = "lecture_by_student")
@Table(name = "lecture_by_student")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class LectureByStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_by_student_code", nullable = false)
    private Long lectureByStudentCode;

    @Column(name = "refund_status", nullable = false)
    private Boolean refundStatus;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "student_code", nullable = false)
    @Where(clause = "member_type = 'STUDENT'")
    private Member student;
}
