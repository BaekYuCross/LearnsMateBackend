package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureCountDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public LectureDTO getLectureById(String lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        return lectureMapper.toDTO(lecture);
    }

    @Override
    public void incrementClickCount(String lectureCode) {
        LectureDTO lectureDTO = getLectureById(lectureCode);
        lectureDTO.setLectureClickCount(lectureDTO.getLectureClickCount() + 1);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        lectureRepository.save(lecture);
    }

    @Override
    public List<LectureDTO> getLecturesByTutorCode(Long tutorCode) {
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(tutorCode, MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        List<Lecture> lectures = lectureRepository.findAllByTutor(tutor);

        if (lectures.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        return lectures.stream()
                .map(lectureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateLectureConfirmStatus(String lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toAcceptConfirmStatus();
        lectureRepository.save(lecture);
        lectureMapper.toDTO(lecture);
    }

    @Override
    public List<MonthlyLectureCountDTO> getMonthlyLectureCounts() {
        List<Object[]> results = lectureRepository.findMonthlyLectureCounts();

        return results.stream()
                .map(result -> new MonthlyLectureCountDTO((String) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<LectureDTO> filterLectureWithPaging(LectureFilterDTO filterDTO, Pageable pageable) {
        Page<ResponseFindLectureVO> lecturePage = lectureRepository.searchByWithPaging(filterDTO, pageable);

        return lecturePage.map(lectureMapper::convertToLectureDTO);
    }

    @Override
    public List<MonthlyLectureCountDTO> getFilteredMonthlyLectureCounts(MonthlyLectureFilterDTO filterDTO) {
        List<Object[]> results = lectureRepository.findFilteredMonthlyLectureCounts(
                filterDTO.getStartYear(),
                filterDTO.getStartMonth(),
                filterDTO.getEndYear(),
                filterDTO.getEndMonth()
        );
        return results.stream()
                .map(result -> new MonthlyLectureCountDTO((String) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalClickCountBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return lectureRepository.sumClickCountBetweenDates(startDate, endDate);
    }

    @Override
    public Integer getClickCountByLectureCodeBetween(String lectureCode, LocalDateTime startDate, LocalDateTime endDate) {
        return lectureRepository.getClickCountByLectureCodeBetweenDates(lectureCode, startDate, endDate);
    }
}
