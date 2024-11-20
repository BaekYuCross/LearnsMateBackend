package intbyte4.learnsmate.voc_category.service;

import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.mapper.VOCCategoryMapper;
import intbyte4.learnsmate.voc_category.repository.VOCCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("vocCategoryService")
@RequiredArgsConstructor
public class VOCCategoryServiceImpl implements VOCCategoryService {

    private final VOCCategoryRepository vocCategoryRepository;
    private final VOCCategoryMapper vocCategoryMapper;

    @Override
    public VOCCategoryDTO findByVocCategoryCode(Integer vocCategoryCode) {
        VOCCategory vocCategory = vocCategoryRepository.findById(vocCategoryCode).orElseThrow(() -> new RuntimeException("vocCategoryCode not found"));
        return vocCategoryMapper.toDTO(vocCategory);
    }

    public List<VOCCategoryDTO> findAll() {
        return vocCategoryRepository.findAll().stream()
                .map(vocCategory -> VOCCategoryDTO.builder()
                        .vocCategoryCode(vocCategory.getVocCategoryCode())
                        .vocCategoryName(vocCategory.getVocCategoryName())
                        .build())
                .collect(Collectors.toList());
    }
}
