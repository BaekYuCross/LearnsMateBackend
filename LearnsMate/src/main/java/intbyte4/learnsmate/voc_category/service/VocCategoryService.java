package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.voc_category.domain.VocCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VocCategoryDTO;

import java.util.List;

public interface VocCategoryService {
    VocCategory findByVocCategoryCode(Integer vocCategoryCode);

    List<VocCategoryDTO> findAll();
}
