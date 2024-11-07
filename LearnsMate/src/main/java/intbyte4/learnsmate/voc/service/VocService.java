package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VocDTO;

import java.util.List;

public interface VocService {
    List<VocDTO> findAllByVoc();

    VocDTO findByVocCode(VocDTO vocDTO);
}
