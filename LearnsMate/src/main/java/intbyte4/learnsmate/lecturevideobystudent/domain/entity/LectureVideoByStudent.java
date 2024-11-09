package intbyte4.learnsmate.lecturevideobystudent.domain.entity;

import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
import intbyte4.learnsmate.videobylecture.domain.entity.VideoByLecture;
import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(LectureVideoByStudent.class)
@Table(name = "lecture_video_by_student")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LectureVideoByStudent {

    @Id
    @ManyToOne
    @JoinColumn(name = "video_code", nullable = false)
    private VideoByLecture videoCode;

    @Id
    @ManyToOne
    @JoinColumn(name = "lecture_by_student_code", nullable = false)
    private LectureByStudent lectureByStudentCode;

    @Column(name = "lecture_status", nullable = false)
    private Boolean lectureStatus;
}
