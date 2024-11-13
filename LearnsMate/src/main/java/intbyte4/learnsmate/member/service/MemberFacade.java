package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.lecture_video_by_student.service.LectureVideoByStudentService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.FindSingleMemberDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.repository.MemberRepository;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.service.VOCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final LectureService lectureService;
    private final IssueCouponService issueCouponService;
    private final VOCService vocService;
    private final MemberMapper memberMapper;
    private final LectureVideoByStudentService lectureVideoByStudentService;

    // 멤버 단일 조회시에 사용되는 서비스
    // memberCode로 학생 조회
    public FindSingleMemberDTO findStudentByStudentCode(Long studentCode) {
        Member member = memberRepository.findById(studentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));

        if(!member.getMemberType().equals(MemberType.STUDENT))
            throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        // 0. 학생 개인정보
        MemberDTO memberDTO = memberMapper.fromMembertoMemberDTO(member);

        // 1. 학생의 강의
        List<LectureVideoProgressDTO> LectureVideoProgressDTOList = lectureVideoByStudentService.getVideoProgressByStudent(studentCode);

        // 2. 학생이 보유 or 사용한 쿠폰
        Map<String, List<IssueCouponDTO>> studentCoupons = issueCouponService.findAllStudentCoupons(studentCode);
        List<IssueCouponDTO> unusedCouponList = studentCoupons.get("unusedCoupons");
        List<IssueCouponDTO> usedCouponList = studentCoupons.get("usedCoupons");

        // 3. 학생이 남긴 voc
        List<VOCDTO> unansweredVOCByMemberList = vocService.findUnansweredVOCByMember(studentCode);
        List<VOCDTO> answeredVOCByMemberList = vocService.findAnsweredVOCByMember(studentCode);

        FindSingleMemberDTO dto = new FindSingleMemberDTO(
                memberDTO,
                LectureVideoProgressDTOList,
                unusedCouponList,
                usedCouponList,
                unansweredVOCByMemberList,
                answeredVOCByMemberList
        );

        return dto;
    }

    public FindSingleMemberDTO findTutorByTutorCode(Long tutorCode) {
        Member member = memberRepository.findById(tutorCode)
                .orElseThrow(() -> new CommonException(StatusEnum.TUTOR_NOT_FOUND));

        if(!member.getMemberType().equals(MemberType.TUTOR))
            throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        // 0. 강사 개인정보
        MemberDTO memberDTO = memberMapper.fromMembertoMemberDTO(member);

        // 1. 강사 강의 정보

        return null;
    }
}
