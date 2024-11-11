package intbyte4.learnsmate.lecture_category_by_lecture.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OneLectureCategoryListDTO {
    private Long lectureCode;
    private List<Integer> lectureCategoryCodeList;
}
