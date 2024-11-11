package intbyte4.learnsmate.lecture_category.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "lecture_category")
@Table(name = "lecture_category")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class LectureCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_category_code")
    private Integer lectureCategoryCode;

    @Column(name = "lecture_category_name")
    private String lectureCategoryName;
}
