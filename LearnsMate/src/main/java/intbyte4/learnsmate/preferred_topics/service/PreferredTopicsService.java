package intbyte4.learnsmate.preferred_topics.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicStatsConvertDTO;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicStatsDTO;
import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import intbyte4.learnsmate.preferred_topics.mapper.PreferredTopicsMapper;
import intbyte4.learnsmate.preferred_topics.repository.PreferredTopicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferredTopicsService {

    private final PreferredTopicsRepository preferredTopicsRepository;
    private final PreferredTopicsMapper preferredTopicsMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureCategoryService lectureCategoryService;

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

    // 특정 멤버의 모든 선호 주제 저장 메서드
    public void savePreferredTopics(List<PreferredTopicsDTO> dtoList) {

        MemberDTO memberDTO = memberService.findById(dtoList.get(0).getMemberCode());
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);

        // LectureCategory 리스트를 가져오기
        List<LectureCategoryDTO> categoryDTOList = dtoList.stream()
                .map(PreferredTopicsDTO::getLectureCategoryCode) // DTO에서 LectureCategoryCode 추출
                .map(lectureCategoryService::findByLectureCategoryCode) // 각 카테고리 코드로 LectureCategory 조회
                .collect(Collectors.toList()); // 결과를 List로 수집

        List<LectureCategory> categoryList = categoryDTOList.stream()
                        .map(dto -> LectureCategory.builder()
                                .lectureCategoryCode(dto.getLectureCategoryCode())
                                .lectureCategoryName(LectureCategoryEnum.valueOf(dto.getLectureCategoryName()))
                                .build())
                                .collect(Collectors.toList());

        List<PreferredTopics> preferredTopicList
                = preferredTopicsMapper.fromPreferredTopicsDTOListtoEntityList(member, categoryList);

        preferredTopicsRepository.saveAll(preferredTopicList);
    }

    public List<PreferredTopicStatsConvertDTO> getMonthlyCategoryStats(LocalDateTime startDate, LocalDateTime endDate) {
        List<PreferredTopicStatsDTO> dtoList = preferredTopicsRepository.findCategoryStatsByMonth(startDate, endDate);

        return dtoList.stream()
                .map(dto -> PreferredTopicStatsConvertDTO.builder()
                        .yearMonth(String.format("%04d-%02d", dto.getYear(), dto.getMonth()))
                        .lectureCategoryName(dto.getLectureCategoryName().toString())
                        .topicCount(dto.getTopicCount())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Long> findStudentsWithSimilarPreferredTopics(Long studentCode) {
        return preferredTopicsRepository.findStudentsWithSimilarPreferredTopics(studentCode);
    }
}
