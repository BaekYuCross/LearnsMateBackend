package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.domain.vo.response.ResponseFindVOCAnswerVO;
import jakarta.transaction.Transactional;

public interface VOCAnswerService {
    VOCAnswerDTO registerVOCAnswer(VOCAnswerDTO vocAnswerDTO);

    VOCAnswerDTO editVOCAnswer(VOCAnswerDTO vocAnswerDTO);

    ResponseFindVOCAnswerVO findById(Long vocAnswerCode);
}
