package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VOCService {
    List<VOCDTO> findAllByVOC();

    VOCDTO findByVOCCode(String vocDTO);

    void updateVOCAnswerStatus(String vocCode, boolean vocAnswerStatus);

    List<VOCDTO> findUnansweredVOCByMember(Long memberCode);

    List<VOCDTO> findAnsweredVOCByMember(Long memberCode);

    Map<Integer, Long> countVOCByCategory(LocalDateTime startDate, LocalDateTime endDate);

    Page<VOCDTO> findAllByVOCWithPaging(Pageable of);

    Page<VOCDTO> filterVOCWithPaging(VOCFilterRequestDTO dto, Pageable pageable);

    List<VOCCategoryCountDTO> getCategoryCounts();

    List<VOCCategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate);
}
