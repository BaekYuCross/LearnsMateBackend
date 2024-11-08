package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.voc_category.domain.VocCategory;
import intbyte4.learnsmate.voc_category.repository.VocCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("vocCategoryService")
@RequiredArgsConstructor
public class VocCategoryServiceImpl implements VocCategoryService {

    private final VocCategoryRepository vocCategoryRepository;

    @Override
    public VocCategory findByVocCategoryCode(Integer vocCategoryCode) {
        log.info("vocCategory 조회 중: {}", vocCategoryCode);
        return vocCategoryRepository.findById(vocCategoryCode).orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));
    }
}
