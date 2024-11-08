package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import jakarta.transaction.Transactional;

public interface VOCAnswerService {
    VOCAnswerDTO registerVOCAnswer(VOCAnswerDTO vocAnswerDTO);

    @Transactional
    VOCAnswerDTO editVOCAnswer(VOCAnswerDTO vocAnswerDTO);
}
