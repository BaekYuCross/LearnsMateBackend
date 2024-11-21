package intbyte4.learnsmate.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDTO {
    private String lectureCategoryCode;
    private String lectureCategoryName;
    private Long count;
}
