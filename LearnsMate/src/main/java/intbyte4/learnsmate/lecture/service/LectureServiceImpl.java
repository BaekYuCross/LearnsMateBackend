package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 전체 강의 조회
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

    // 강의 단건 조회
    @Override
    public LectureDTO getLectureById(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        return lectureMapper.toDTO(lecture);
    }

    // 유저별 조회에서 count++ lectureClickCount int 올리기
    @Override
    public LectureDTO getLecturesByStudentCode(Long studentCode) {
        // 학생 정보 조회
        MemberDTO studentDTO = memberService.findMemberByMemberCode(studentCode, MemberType.STUDENT);
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        // 학생이 조회하려는 강의를 가져옴
        LectureDTO lectureDTO = getLectureById(studentCode);

        // 해당 강의의 클릭 수 증가
        Lecture lecture = lectureMapper.toEntity(lectureDTO,student);
        lecture.incrementClickCount();  // 클릭 수를 증가시키는 메서드 추가 필요

        // 강의 정보 업데이트
        lectureRepository.save(lecture);

        // 업데이트된 강의를 DTO로 변환하여 반환
        return lectureMapper.toDTO(lecture);
    }


    // 강사별 강의 모두 조회
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

    // 강의 수정
    @Override
    @Transactional
    public LectureDTO updateLecture(LectureDTO lectureDTO) {
        Lecture lecture = lectureRepository.findById(lectureDTO.getLectureCode())
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));

        lecture.toUpdate(lectureDTO);
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }

    // 강의별 계약과정이 강의 코드가 7개 라면 강의컬럼의 승인여부 true로 변환
    @Override
    @Transactional
    public LectureDTO updateLectureConfirmStatus(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toAcceptConfirmStatus();
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }
}
