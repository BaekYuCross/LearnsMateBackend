package intbyte4.learnsmate.lecture_category_by_lecture.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LectureCategoryByLectureDTO {

    private Long lectureCategoryByLectureCode;
    private String lectureCode;
    private Integer lectureCategoryCode;
}
