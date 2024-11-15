package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureCountDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public List<LectureDTO> getAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();
        if (lectureList.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }
        return lectureList.stream()
                .map(lectureMapper::toDTO)
                .collect(Collectors.toList());
    }

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

    // 카테고리별 강의 조회
//    public List<Lecture> filterLectures(LectureFilterDTO filter) {
//        Specification<Lecture> spec = LectureSpecifications.filterByCriteria(filter);
//        return lectureRepository.findAll(spec);
//    }

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
        List<Lecture> lectures = lectureRepository.findAll();

        Map<String, Long> groupedByMonth = lectures.stream()
                .collect(Collectors.groupingBy(
                        lecture -> lecture.getCreatedAt().getYear() + "-" +
                                String.format("%02d", lecture.getCreatedAt().getMonthValue()),
                        Collectors.counting()
                ));

        return groupedByMonth.entrySet().stream()
                .map(entry -> new MonthlyLectureCountDTO(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparing(MonthlyLectureCountDTO::getDate))
                .collect(Collectors.toList());
    }
}
