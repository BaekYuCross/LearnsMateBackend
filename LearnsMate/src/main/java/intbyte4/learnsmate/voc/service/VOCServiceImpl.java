package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.repository.VOCRepository;
import intbyte4.learnsmate.voc_category.domain.dto.VocCategoryDTO;
import intbyte4.learnsmate.voc_category.service.VocCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final VocCategoryService vocCategoryService;

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
    public VOCDTO findByVOCCode(Long vocCode) {
        log.info("VOC 단 건 조회 중: {}", vocCode);
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));

        return vocMapper.fromEntityToDTO(voc);
    }

    @Override
    public void updateVOCAnswerStatus(Long vocCode, boolean vocAnswerStatus) {
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
    public List<VOCDTO> filterVOC(VOCDTO vocDTO, MemberDTO memberDTO) {
        log.info("VOC 필터링 요청: {}", vocDTO);
        List<VOC> filteredVOCList = vocRepository.searchBy(vocDTO, memberDTO);

        if (filteredVOCList.isEmpty()) {
            log.info("필터 조건에 맞는 VOC가 없습니다.");
            throw new CommonException(StatusEnum.VOC_NOT_FOUND);
        }

        List<VOCDTO> vocDTOList = filteredVOCList.stream()
                .map(vocMapper::fromEntityToDTO)
                .collect(Collectors.toList());

        log.info("필터링된 VOC 수: {}", vocDTOList.size());
        return vocDTOList;
    }

    @Override
    public Map<Integer, Long> countVOCByCategory(){
        List<VocCategoryDTO> vocCategoryDTOList = vocCategoryService.findAll();

        Map<Integer, Long> categoryCountMap = new HashMap<>();

        vocCategoryDTOList.forEach(category -> {
            int categoryCode = category.getVocCategoryCode();
            long count = vocRepository.countByVocCategory_VocCategoryCode(categoryCode);
            categoryCountMap.put(categoryCode, count);
        });

        return categoryCountMap;
    }
}
