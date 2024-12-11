package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;

import java.util.List;

public interface VOCCategoryService {
    VOCCategoryDTO findByVocCategoryCode(Integer vocCategoryCode);

    List<VOCCategoryDTO> findAll();
}
