package intbyte4.learnsmate.lecturebystudent.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.common.mapper.MemberMapper;
import intbyte4.learnsmate.lecturebystudent.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecturebystudent.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecturebystudent.repository.LectureByStudentRepository;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
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

    // 학생별 모든 강의 조회
    @Override
    public List<LectureByStudentDTO> findByStudentCode(Long lectureByStudentCode) {
//        MemberDTO student = memberService.findByMemberCode(lectureByStudentCode);
//        List<LectureByStudent> lecturesByStudent = lectureByStudentRepository.findByStudentCode(student.getMemberCode());
//
//        if (lecturesByStudent.isEmpty()) {
//            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
//        }
//
//        return lecturesByStudent.stream()
//                .map(lectureByStudentMapper::toDTO)
//                .collect(Collectors.toList());
        return null;
    }


}

