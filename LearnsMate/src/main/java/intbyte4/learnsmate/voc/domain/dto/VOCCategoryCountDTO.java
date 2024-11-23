package intbyte4.learnsmate.voc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VOCCategoryCountDTO {
    private Integer vocCategoryCode;
    private String vocCategoryName;
    private Long count;
}