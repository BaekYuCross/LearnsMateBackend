package intbyte4.learnsmate.lecture_category.domain.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureCategoryDTO {
    private Integer lectureCategoryCode;
    private String lectureCategoryName;
}
