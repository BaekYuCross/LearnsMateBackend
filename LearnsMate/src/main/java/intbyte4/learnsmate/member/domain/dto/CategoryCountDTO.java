package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDTO {
    private Integer lectureCategoryCode;
    private LectureCategoryEnum lectureCategoryName;
    private Long count;
}
