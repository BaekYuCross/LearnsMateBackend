package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.voc_category.domain.VocCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VocCategoryDTO;
import intbyte4.learnsmate.voc_category.repository.VocCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("vocCategoryService")
@RequiredArgsConstructor
public class VocCategoryServiceImpl implements VocCategoryService {

    private final VocCategoryRepository vocCategoryRepository;

    @Override
    public VocCategory findByVocCategoryCode(Integer vocCategoryCode) {
        log.info("vocCategory 조회 중: {}", vocCategoryCode);
        return vocCategoryRepository.findById(vocCategoryCode).orElseThrow(() -> new RuntimeException("vocCategoryCode not found"));
    }

    public List<VocCategoryDTO> findAll() {
        return vocCategoryRepository.findAll().stream()
                .map(vocCategory -> VocCategoryDTO.builder()
                        .vocCategoryCode(vocCategory.getVocCategoryCode())
                        .vocCategoryName(vocCategory.getVocCategoryName())
                        .build())
                .collect(Collectors.toList());
    }
}
