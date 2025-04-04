package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCClientDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.repository.VOCRepository;
import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.service.VOCCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final MemberMapper memberMapper;

    @Override
    public VOCDTO findByVOCCode(String vocCode) {
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));

        VOCDTO vocDTO = vocMapper.fromEntityToDTO(voc);
        return vocDTO;
    }

    @Override
    public void updateVOCAnswerStatus(String vocCode, boolean vocAnswerStatus) {
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));
        voc.setVocAnswerStatus(vocAnswerStatus);
        vocRepository.save(voc);
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
    public List<VOCDTO> findUnansweredVOC() {
        List<VOC> vocList = vocRepository.findTop5ByVocAnswerStatusFalseOrderByCreatedAtDesc();
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

//    @Override
//    public Page<VOCDTO> findAllByVOCWithPaging(Pageable pageable) {
//        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<VOC> vocPage = vocRepository.findAll(pageRequest);
//        return vocPage.map(vocMapper::fromEntityToDTO);
//    }

    // 현재 시간보다 이전의 데이터를 가져오기 위해 해둔 것 실제라면 위의 서비스 로직으로 구현해야 함
    @Override
    public Page<VOCDTO> findAllByVOCWithPaging(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<VOC> vocPage = vocRepository.findAllBeforeNow(now, sortedPageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }

    // 필터링x 정렬o
    @Override
    public Page<VOCDTO> findAllByVOCWithPagingWithSort(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Page<VOC> vocPage = vocRepository.findAllBeforeNowWithSort(now, pageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }

    @Override
    public Page<VOCDTO> filterVOCWithPaging(VOCFilterRequestDTO dto, Pageable pageable) {
        Page<VOC> vocPage = vocRepository.searchByWithPaging(dto, pageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }

    // 필터링o 정렬o
    @Override
    public Page<VOCDTO> filterVOCWithPagingWithSort(VOCFilterRequestDTO dto, Pageable pageable) {
        Page<VOC> vocPage = vocRepository.searchByWithPagingWithSort(dto, pageable);
        return vocPage.map(vocMapper::fromEntityToDTO);
    }

    @Override
    public List<VOCCategoryCountDTO> getCategoryCounts() {
        return vocRepository.countVocByCategory();
    }

    @Override
    public List<VOCCategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return vocRepository.countVocByCategoryWithinDateRange(startDate, endDate);
    }

    @Override
    public List<VOCDTO> findAllByFilter(VOCFilterRequestDTO dto) {
        List<VOC> vocList = vocRepository.findAllByFilter(dto);
        return vocList.stream()
                .map(vocMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VOCDTO> findAllVOCs() {
        List<VOC> vocList = vocRepository.findAll();
        return vocList.stream()
                .map(vocMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VOCDTO saveVOC(VOCDTO dto, MemberDTO memberDTO, VOCCategoryDTO vocCategoryDTO) {

        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);
        VOCCategory vocCategory = VOCCategory.builder()
                .vocCategoryCode(vocCategoryDTO.getVocCategoryCode())
                .vocCategoryName(vocCategoryDTO.getVocCategoryName()).build();

        VOC voc = VOC.builder()
                .vocContent(dto.getVocContent())
                .vocAnswerStatus(false)
                .vocAnswerSatisfaction(null)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .vocCategory(vocCategory)
                .member(member)
                .build();

        vocRepository.save(voc);

        return vocMapper.fromEntityToDTO(voc);
    }

    // 회원의 전체 voc 조회
    @Override
    public List<VOCClientDTO> findAllClientVOC(Long memberCode) {
        List<VOCClientDTO> dtoList = vocRepository.findAllClientVOC(memberCode);

        return dtoList;
    }

    @Override
    public void updateVocSatisfaction(String vocCode, Long satisfaction) {
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));

        voc.updateSatisfaction(satisfaction);
        vocRepository.save(voc);
    }
}
