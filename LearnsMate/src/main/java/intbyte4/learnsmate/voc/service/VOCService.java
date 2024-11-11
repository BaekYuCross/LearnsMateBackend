package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VOCDTO;

import java.util.List;

public interface VOCService {
    List<VOCDTO> findAllByVOC();

    VOCDTO findByVOCCode(Long vocDTO);

    void updateVOCAnswerStatus(Long vocCode, boolean vocAnswerStatus);

    List<VOCDTO> findUnansweredVOCByMember(Long memberCode);

    List<VOCDTO> findAnsweredVOCByMember(Long memberCode);
}
