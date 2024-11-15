package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;

import java.util.List;
import java.util.Map;

public interface VOCService {
    List<VOCDTO> findAllByVOC();

    VOCDTO findByVOCCode(Long vocDTO);

    void updateVOCAnswerStatus(Long vocCode, boolean vocAnswerStatus);

    List<VOCDTO> findUnansweredVOCByMember(Long memberCode);

    List<VOCDTO> findAnsweredVOCByMember(Long memberCode);

    List<VOCDTO> filterVOC(VOCDTO vocDTO, MemberDTO memberDTO);

    Map<Integer, Long> countVOCByCategory();
}
