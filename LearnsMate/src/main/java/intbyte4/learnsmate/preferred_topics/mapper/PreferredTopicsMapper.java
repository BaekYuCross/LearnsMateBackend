package intbyte4.learnsmate.preferred_topics.mapper;

import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import intbyte4.learnsmate.preferred_topics.domain.vo.response.ResponseFindPreferredTopicsVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PreferredTopicsMapper {

    public List<PreferredTopicsDTO> fromEntityToDTO(List<PreferredTopics> entityList) {
        return entityList.stream()
                .map(entity -> PreferredTopicsDTO.builder()
                        .preferredTopicCode(entity.getPreferredTopicCode())
                        .memberCode(entity.getMember().getMemberCode())
                        .lectureCategoryCode(entity.getLectureCategory().getLectureCategoryCode())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseFindPreferredTopicsVO> fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(List<PreferredTopicsDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> ResponseFindPreferredTopicsVO.builder()
                        .preferredTopicCode(dto.getPreferredTopicCode())
                        .memberCode(dto.getMemberCode())
                        .lectureCategoryCode(dto.getLectureCategoryCode())
                        .build())
                .collect(Collectors.toList());
    }
}
