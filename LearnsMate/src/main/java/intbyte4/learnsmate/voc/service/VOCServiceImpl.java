package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.repository.VOCRepository;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.service.VOCCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("vocService")
@RequiredArgsConstructor
public class VOCServiceImpl implements VOCService {

    private final VOCRepository vocRepository;
    private final VOCMapper vocMapper;
    private final VOCCategoryService vocCategoryService;

    @Override
    public List<VOCDTO> findAllByVOC() {
        log.info("VOC 전체 조회 중");
        List<VOC> vocList = vocRepository.findAll();
        List<VOCDTO> VOCDTOList = new ArrayList<>();

        for (VOC voc : vocList) {
            VOCDTOList.add(vocMapper.fromEntityToDTO(voc));
        }
        return VOCDTOList;
    }

    @Override
    public VOCDTO findByVOCCode(String vocCode) {
        log.info("VOC 단 건 조회 중: {}", vocCode);
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));

        return vocMapper.fromEntityToDTO(voc);
    }

    @Override
    public void updateVOCAnswerStatus(String vocCode, boolean vocAnswerStatus) {
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));
        voc.setVocAnswerStatus(vocAnswerStatus);
        vocRepository.save(voc);
        log.info("VOC 답변 상태가 {}로 업데이트됐습니다.: {}", vocAnswerStatus, voc);
    }

    @Override
    public List<VOCDTO> findUnansweredVOCByMember(Long memberCode) {
        List<VOC> vocList = vocRepository.findUnansweredVOCByMember(memberCode);
        return vocList.stream()
                .map(vocMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VOCDTO> findAnsweredVOCByMember(Long memberCode) {
        List<VOC> vocList = vocRepository.findAnsweredVOCByMember(memberCode);
        return vocList.stream()
                .map(vocMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Long> countVOCByCategory(LocalDateTime startDate, LocalDateTime endDate){
        List<VOCCategoryDTO> VOCCategoryDTOList = vocCategoryService.findAll();

        Map<Integer, Long> categoryCountMap = new HashMap<>();

        VOCCategoryDTOList.forEach(category -> {
            int vocCategoryCode = category.getVocCategoryCode();
            long count;
            if(startDate != null && endDate != null) {
                count = vocRepository.countByVocCategoryCodeAndDateRange(vocCategoryCode, startDate, endDate);
            } else {
                count = vocRepository.countByVocCategory_VocCategoryCode(vocCategoryCode);
            }
            categoryCountMap.put(vocCategoryCode, count);
        });

        return categoryCountMap;
    }

    @Override
    public Page<VOCDTO> findAllByVOCWithPaging(Pageable pageable) {
        Page<VOC> vocPage = vocRepository.findAll(pageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }

    @Override
    public Page<VOCDTO> filterVOCWithPaging(VOCFilterRequestDTO dto, Pageable pageable) {
        Page<VOC> vocPage = vocRepository.searchByWithPaging(dto, pageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }
}
