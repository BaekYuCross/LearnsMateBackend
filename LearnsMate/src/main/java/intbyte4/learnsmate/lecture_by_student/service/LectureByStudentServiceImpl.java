package intbyte4.learnsmate.lecture_by_student.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
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

    // 학생별 모든 강의 조회 (refund_status 가 true인것만)
    @Override
    public List<LectureByStudentDTO> findByStudentCode(Long studentCode) {

        MemberDTO studentDTO = memberService.findMemberByMemberCode(studentCode, MemberType.STUDENT);
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        // 환불 상태가 true인 강의만 조회
        List<LectureByStudent> lecturesByStudent = lectureByStudentRepository.findByStudentAndRefundStatus(student, true);

        if (lecturesByStudent.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        return lecturesByStudent.stream()
                .map(lectureByStudentMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 강의별 학생코드 개수 조회 refund_status 가 true인것만



}

