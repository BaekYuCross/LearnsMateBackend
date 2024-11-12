package intbyte4.learnsmate.lecture_by_student.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureByStudentServiceImpl implements LectureByStudentService {

    private final LectureByStudentRepository lectureByStudentRepository;
    private final LectureByStudentMapper lectureByStudentMapper;
    private final LectureCategoryService lectureCategoryService;
    private final MemberService memberService;
    private final LectureService lectureService;
    private final LectureCategoryMapper lectureCategoryMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;

    // 학생별 모든 강의 조회 (refund_status 가 true인것만)
    @Override
    public List<LectureByStudentDTO> findByStudentCode(Long studentCode) {

        MemberDTO studentDTO = memberService.findMemberByMemberCode(studentCode, MemberType.STUDENT);
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        List<LectureByStudent> lecturesByStudent = lectureByStudentRepository.findByStudentAndOwnStatus(student, true);

        if (lecturesByStudent.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        return lecturesByStudent.stream()
                .map(lectureByStudentMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 강의별 학생코드 개수 조회 (refund_status 가 true인것만)
    @Override
    public long countStudentsByLectureAndOwnStatus(Long lectureCode) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        // 환불되지 않은 수강생의 누적 명수 조회
        // --> refund_status가 보유상태라고 적혀있지만 영어는 환불상태.. 뭐가 맞을까?
        return lectureByStudentRepository.countByLectureAndOwnStatus(lecture, true);
    }

    @Override
    @Transactional
    // 파라미터로 lecture를 받는게 맞는지 우선 모르겠음
    public void updateOwnStatus(Lecture lecture) {
        // removeLecture에서 파라미터로 받은 lectureCode를 컬럼으로 가지고 있는 애들의 lectureByStudentCode 뽑아내기
        List<Long> lectureByStudentCodes = lectureByStudentRepository.findLectureByStudentCodesByLectureCode(lecture.getLectureCode());
        List<LectureByStudent> lectureByStudents = lectureByStudentRepository.findAllById(lectureByStudentCodes);
        for (LectureByStudent lectureByStudent : lectureByStudents) {
            lectureByStudent.changeOwnStatus();
        }

        lectureByStudentRepository.saveAll(lectureByStudents);
    }
}

