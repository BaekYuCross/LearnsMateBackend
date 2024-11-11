package intbyte4.learnsmate.preferred_topics.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import intbyte4.learnsmate.preferred_topics.mapper.PreferredTopicsMapper;
import intbyte4.learnsmate.preferred_topics.repository.PreferredTopicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferredTopicsService {

    private final PreferredTopicsRepository preferredTopicsRepository;
    private final PreferredTopicsMapper preferredTopicsMapper;

    public List<PreferredTopicsDTO> findAll() {

        List<PreferredTopics> entityList = preferredTopicsRepository.findAll();

        return preferredTopicsMapper.fromEntityToDTO(entityList);
    }

    public List<PreferredTopicsDTO> findById(Long topicsCode) {

        PreferredTopics preferredTopics = preferredTopicsRepository.findById(topicsCode)
                .orElseThrow(() -> new CommonException(StatusEnum.PREFERRED_TOPICS_NOT_FOUND));

        // 엔티티를 DTO로 변환하여 리스트로 반환
        return preferredTopicsMapper.fromEntityToDTO(List.of(preferredTopics));
    }
}
