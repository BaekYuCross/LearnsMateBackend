package intbyte4.learnsmate.lecture_by_student.service;

import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

