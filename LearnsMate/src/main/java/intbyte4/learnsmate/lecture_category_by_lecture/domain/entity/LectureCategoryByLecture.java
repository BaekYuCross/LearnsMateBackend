package intbyte4.learnsmate.lecture_category_by_lecture.domain.entity;


import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "lectureCategoryByLecture")
@Table(name = "lecture_category_by_lecture")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LectureCategoryByLecture {

    @Id
    @Column(name = "lecture_category_by_lecture_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lectureCategoryByLectureCode;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "lecture_category_code", nullable = false)
    private LectureCategory lectureCategory;
}
