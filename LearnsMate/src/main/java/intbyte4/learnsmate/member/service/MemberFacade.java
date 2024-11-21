package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.lecture_video_by_student.service.LectureVideoByStudentService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.CategoryCountDTO;
import intbyte4.learnsmate.member.domain.dto.FindSingleStudentDTO;
import intbyte4.learnsmate.member.domain.dto.FindSingleTutorDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.pagination.MemberPageResponse;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.repository.MemberRepository;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.service.VideoByLectureFacade;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.service.VOCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberFacade {

    private final MemberRepository memberRepository;
    private final IssueCouponService issueCouponService;
    private final VOCService vocService;
    private final MemberMapper memberMapper;
    private final LectureVideoByStudentService lectureVideoByStudentService;
    private final VideoByLectureFacade videoByLectureFacade;
    private final LectureCategoryByLectureService lectureCategoryByLectureService;

    // 멤버 단일 조회시에 사용되는 서비스
    // memberCode로 학생 조회
    public FindSingleStudentDTO findStudentByStudentCode(Long studentCode) {
        Member member = memberRepository.findById(studentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));

        if(!member.getMemberType().equals(MemberType.STUDENT))
            throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        // 0. 학생 개인정보
        MemberDTO studentDTO = memberMapper.fromMembertoMemberDTO(member);

        // 1. 학생의 강의
        List<LectureVideoProgressDTO> LectureVideoProgressDTOList = lectureVideoByStudentService.getVideoProgressByStudent(studentCode);

        // 2. 학생이 보유 or 사용한 쿠폰
        Map<String, List<IssueCouponDTO>> studentCoupons = issueCouponService.findAllStudentCoupons(studentCode);
        List<IssueCouponDTO> unusedCouponList = studentCoupons.get("unusedCoupons");
        List<IssueCouponDTO> usedCouponList = studentCoupons.get("usedCoupons");

        // 3. 학생이 남긴 voc
        List<VOCDTO> unansweredVOCByMemberList = vocService.findUnansweredVOCByMember(studentCode);
        List<VOCDTO> answeredVOCByMemberList = vocService.findAnsweredVOCByMember(studentCode);

        FindSingleStudentDTO dto = new FindSingleStudentDTO(
                studentDTO,
                LectureVideoProgressDTOList,
                unusedCouponList,
                usedCouponList,
                unansweredVOCByMemberList,
                answeredVOCByMemberList
        );

        return dto;
    }

    public FindSingleTutorDTO findTutorByTutorCode(Long tutorCode) {
        Member member = memberRepository.findById(tutorCode)
                .orElseThrow(() -> new CommonException(StatusEnum.TUTOR_NOT_FOUND));

        if(!member.getMemberType().equals(MemberType.TUTOR))
            throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        // 0. 강사 개인정보
        MemberDTO tutorDTO = memberMapper.fromMembertoMemberDTO(member);

        // 1. 강사 강의 정보
        List<CountVideoByLectureDTO> tutorLectureDetailList = videoByLectureFacade.getVideoByLecture(tutorCode);

        FindSingleTutorDTO dto = new FindSingleTutorDTO(
                tutorDTO,
                tutorLectureDetailList
        );

        return dto;
    }

    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberType(int page, int size, MemberType memberType) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findByMemberFlagTrueAndMemberType(memberType, pageable);

        List<ResponseFindMemberVO> responseVOList = memberPage.getContent().stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        return new MemberPageResponse<>(
                responseVOList,
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.getNumber(),
                memberPage.getSize()
        );
    }

    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByCursor(LocalDateTime cursor, int size, MemberType memberType) {
        PageRequest pageable = PageRequest.of(0, size);
        List<Member> members = memberRepository.findMembersByCursorAndMemberType(cursor, memberType, pageable);

        List<ResponseFindMemberVO> responseVOList = members.stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        return new MemberPageResponse<>(
                responseVOList,
                responseVOList.size(),
                1,
                0,
                size
        );
    }

    public List<CategoryCountDTO> getCategoryCounts() {
        return lectureCategoryByLectureService.countLecturesByCategory();
    }

    public List<CategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return lectureCategoryByLectureService.countLecturesByCategoryWithinDateRange(startDate, endDate);
    }

    public Map<String, Double> calculateCategoryPercentage(List<CategoryCountDTO> categoryCounts) {
        long total = categoryCounts.stream()
                .mapToLong(CategoryCountDTO::getCount)
                .sum();

        return categoryCounts.stream()
                .collect(Collectors.toMap(
                        CategoryCountDTO::getLectureCategoryName,
                        dto -> (double) dto.getCount() / total * 100
                ));
    }

}
