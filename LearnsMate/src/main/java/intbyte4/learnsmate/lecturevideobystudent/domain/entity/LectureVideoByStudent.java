package intbyte4.learnsmate.lecturevideobystudent.domain.entity;


import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
import intbyte4.learnsmate.videobylecture.domain.entity.VideoByLecture;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "lecture_video_by_student")
@Table(name = "lecture_video_by_student")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class LectureVideoByStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_code", nullable = false)
    private Long videoCode;

    @Id
    @Column(name = "lecture_by_student_code", nullable = false)
    private Long lectureByStudentCode;

    @Column(name = "lecture_status", nullable = false)
    private Boolean lectureStatus;

    @ManyToOne
    @JoinColumn(name = "video_code", nullable = false)
    private VideoByLecture videoByLecture;

    @ManyToOne
    @JoinColumn(name = "lecture_by_student_code", nullable = false)
    private LectureByStudent lectureByStudent;


}
