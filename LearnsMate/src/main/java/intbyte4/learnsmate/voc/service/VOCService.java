package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;

import java.util.List;

public interface VOCService {
    List<ResponseFindVOCVO> findAllByVOC();

    ResponseFindVOCVO findByVOCCode(Long vocDTO);
}
