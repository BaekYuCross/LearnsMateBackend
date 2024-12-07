package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;

public interface VOCAnswerService {
    VOCAnswerDTO registerVOCAnswer(VOCAnswerDTO vocAnswerDTO);

    VOCAnswerDTO editVOCAnswer(VOCAnswerDTO vocAnswerDTO);

    VOCAnswerDTO findById(Long vocAnswerCode);

    VOCAnswerDTO findByVOCCode(String vocCode);
}
