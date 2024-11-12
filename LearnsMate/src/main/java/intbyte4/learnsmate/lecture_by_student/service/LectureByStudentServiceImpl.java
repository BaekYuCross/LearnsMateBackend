package intbyte4.learnsmate.lecture_by_student.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureByStudentServiceImpl implements LectureByStudentService {

    private final LectureByStudentRepository lectureByStudentRepository;
    private final LectureByStudentMapper lectureByStudentMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 학생별 모든 강의 조회 (ownStatus가 true인것만)
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

    // 강의별 학생코드 개수 조회 (ownStatus 가 true인것만)
    @Override
    public long countStudentsByLectureAndRefundStatus(Long lectureCode) {
        // 강의 코드에 해당하는 환불되지 않은 수강생의 개수를 조회
        return lectureByStudentRepository.countByLectureAndOwnStatus(lectureCode);
    }

    // 강의 코드에 해당하는 결제 금액을 합산
    @Override
    public int calculateTotalRevenue(Long lectureCode) {
        return lectureByStudentRepository.calculateTotalRevenueByLecture(lectureCode);
    }

    @Override
    public void registerLectureByStudent(LectureByStudentDTO lectureByStudentDTO, Lecture lecture, Member member) {
        LectureByStudent lectureByStudent = lectureByStudentMapper.toEntity(lectureByStudentDTO, lecture, member);

        lectureByStudentRepository.save(lectureByStudent);
    }

    @Override
    public Long findStudentCodeByLectureCode(Lecture lecture) {
        LectureByStudent lectureByStudent = lectureByStudentRepository.findByLecture(lecture);
        return lectureByStudent.getLectureByStudentCode();
    }

    @Override
    public LectureByStudentDTO findByLectureAndStudent(Lecture lecture, Member member) {
        LectureByStudent lectureByStudent = lectureByStudentRepository.findByLectureAndStudent(lecture, member);
        return lectureByStudentMapper.toDTO(lectureByStudent);
    }
}

