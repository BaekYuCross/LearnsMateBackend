package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.voc_category.domain.VocCategory;

public interface VocCategoryService {
    VocCategory findByVocCategoryCode(Integer vocCategoryCode);
}
