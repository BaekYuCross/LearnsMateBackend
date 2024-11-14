package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface VOCRepositoryCustom {
    List<VOC> searchBy(VOCDTO request, MemberDTO memberDTO);
}
