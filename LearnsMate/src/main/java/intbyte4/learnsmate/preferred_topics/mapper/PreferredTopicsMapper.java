package intbyte4.learnsmate.preferred_topics.mapper;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicStatsConvertDTO;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import intbyte4.learnsmate.preferred_topics.domain.vo.request.RequestSavePreferredHistoryVO;
import intbyte4.learnsmate.preferred_topics.domain.vo.response.ResponseFindMonthlyStatsVO;
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

    public List<PreferredTopicsDTO> fromRequestSavePreferredHistoryVOtoPreferredTopicsDTOList(RequestSavePreferredHistoryVO request) {
        return request.getLectureCategoryCodeList().stream()
                .map(vo -> PreferredTopicsDTO.builder()
                        .memberCode(request.getMemberCode())
                        .lectureCategoryCode(vo)
                        .build())
                .collect(Collectors.toList());
    }

    public List<PreferredTopics> fromPreferredTopicsDTOListtoEntityList(Member member, List<LectureCategory> categoryList) {
        return categoryList.stream()
                .map(category -> PreferredTopics.builder()
                        .member(member)
                        .lectureCategory(category)
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseFindMonthlyStatsVO> fromPreferredTopicStatsConvertDTOtoVO(List<PreferredTopicStatsConvertDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> ResponseFindMonthlyStatsVO.builder()
                        .yearMonth(dto.getYearMonth())
                        .lectureCategoryName(dto.getLectureCategoryName())
                        .topicCount(dto.getTopicCount())
                        .build())
                .collect(Collectors.toList());
    }
}
