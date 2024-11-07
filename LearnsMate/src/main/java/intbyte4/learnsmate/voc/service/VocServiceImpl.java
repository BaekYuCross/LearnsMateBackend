package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.Voc;
import intbyte4.learnsmate.voc.domain.dto.VocDTO;
import intbyte4.learnsmate.voc.mapper.VocMapper;
import intbyte4.learnsmate.voc.repository.VocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("vocService")
@RequiredArgsConstructor
public class VocServiceImpl implements VocService {

    private final VocRepository vocRepository;
    private final VocMapper vocMapper;

    @Override
    public List<VocDTO> findAllByVoc() {
        log.info("템플릿 전체 조회 중");
        List<Voc> vocList = vocRepository.findAll();
        List<VocDTO> vocDTOList = new ArrayList<>();

        for (Voc voc : vocList) {
            vocDTOList.add(vocMapper.fromEntityToDto(voc));
        }

        return vocDTOList;
    }
}
