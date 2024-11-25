package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class CategoryStatDTO {
    private LectureCategoryEnum category;
    private Long count;
    private Double percentage;
}
