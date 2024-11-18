package intbyte4.learnsmate.voc_category.mapper;

import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class VOCCategoryMapper {
    public VOCCategoryDTO toDTO(VOCCategory vocCategory) {
        return VOCCategoryDTO.builder()
                .vocCategoryCode(vocCategory.getVocCategoryCode())
                .vocCategoryName(vocCategory.getVocCategoryName())
                .build();
    }

    public VOCCategory toEntity(VOCCategoryDTO vocCategoryDTO) {
        return VOCCategory.builder()
                .vocCategoryCode(vocCategoryDTO.getVocCategoryCode())
                .vocCategoryName(vocCategoryDTO.getVocCategoryName())
                .build();
    }
}
