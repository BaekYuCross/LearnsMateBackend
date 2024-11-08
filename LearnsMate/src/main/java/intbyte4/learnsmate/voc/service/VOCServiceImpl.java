package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.repository.VOCRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("vocService")
@RequiredArgsConstructor
public class VOCServiceImpl implements VOCService {

    private final VOCRepository vocRepository;
    private final VOCMapper vocMapper;

    @Override
    public List<VOCDTO> findAllByVOC() {
        log.info("VOC 전체 조회 중");
        List<VOC> vocList = vocRepository.findAll();
        List<VOCDTO> VOCDTOList = new ArrayList<>();

        for (VOC voc : vocList) {
            VOCDTOList.add(vocMapper.fromEntityToDTO(voc));
        }
        return VOCDTOList;
    }

    @Override
    public VOCDTO findByVOCCode(Long vocCode) {
        log.info("VOC 단 건 조회 중: {}", vocCode);
        VOC voc = vocRepository.findById(vocCode)
                .orElseThrow(() -> new CommonException(StatusEnum.VOC_NOT_FOUND));

        return vocMapper.fromEntityToDTO(voc);
    }
}
