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

    // 모든 선호 주제 조회
    public List<PreferredTopicsDTO> findAll() {

        List<PreferredTopics> entityList = preferredTopicsRepository.findAll();

        return preferredTopicsMapper.fromEntityToDTO(entityList);
    }

    // 특정 선호 주제 조회
    public List<PreferredTopicsDTO> findById(Long topicsCode) {

        PreferredTopics preferredTopics = preferredTopicsRepository.findById(topicsCode)
                .orElseThrow(() -> new CommonException(StatusEnum.PREFERRED_TOPICS_NOT_FOUND));

        return preferredTopicsMapper.fromEntityToDTO(List.of(preferredTopics));
    }

    // 특정 멤버의 모든 선호주제 조회 -> 프론트에서 처리
    public List<PreferredTopicsDTO> findAllByMemberCode(Long memberCode) {

        List<PreferredTopics> entityList = preferredTopicsRepository.findByMember_MemberCode(memberCode);

        if(entityList.isEmpty() || entityList == null){
            throw new CommonException(StatusEnum.MEMBER_PREFERRED_TOPICS_NOT_FOUND);
        }

        return preferredTopicsMapper.fromEntityToDTO(entityList);
    }
}
