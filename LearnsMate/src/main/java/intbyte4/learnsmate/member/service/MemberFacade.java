package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
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
import intbyte4.learnsmate.payment.service.PaymentService;
import intbyte4.learnsmate.preferred_topics.service.PreferredTopicsService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.service.VideoByLectureFacade;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.service.VOCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
    private final PaymentService paymentService;
    private final PreferredTopicsService preferredTopicsService;

    // 멤버 단일 조회시에 사용되는 서비스
    // memberCode로 학생 조회
    public FindSingleStudentDTO findStudentByStudentCode(Long studentCode) {
        Member member = memberRepository.findById(studentCode).orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));
        if (!member.getMemberType().equals(MemberType.STUDENT)) throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        MemberDTO studentDTO = memberMapper.fromMembertoMemberDTO(member);

        List<LectureVideoProgressDTO> lectureVideoProgressDTOList = lectureVideoByStudentService.getVideoProgressByStudent(studentCode);

        Map<String, List<IssueCouponDTO>> studentCoupons = issueCouponService.findAllStudentCoupons(studentCode);
        List<IssueCouponDTO> unusedCouponList = studentCoupons.get("unusedCoupons");
        List<IssueCouponDTO> usedCouponList = studentCoupons.get("usedCoupons");

        List<VOCDTO> unansweredVOCByMemberList = vocService.findUnansweredVOCByMember(studentCode);
        List<VOCDTO> answeredVOCByMemberList = vocService.findAnsweredVOCByMember(studentCode);

        // 강의 추천 로직 추가
        String latestLectureCode = paymentService.findLatestLectureCodeByStudent(studentCode);

        List<Long> similarStudents = preferredTopicsService.findStudentsWithSimilarPreferredTopics(studentCode);

        List<String> recommendedLectureCodes = Collections.emptyList();
        if (!similarStudents.isEmpty()) {
            Pageable pageable = PageRequest.of(0, 3); // 상위 3개만 가져오기
            List<Object[]> recommendedLectures = paymentService.findRecommendedLectures(similarStudents, latestLectureCode, studentCode, pageable);

            recommendedLectureCodes = recommendedLectures.stream()
                    .map(record -> (String) record[0])
                    .collect(Collectors.toList());
        }

        return new FindSingleStudentDTO(
                studentDTO,
                lectureVideoProgressDTOList,
                unusedCouponList,
                usedCouponList,
                unansweredVOCByMemberList,
                answeredVOCByMemberList,
                recommendedLectureCodes
        );
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
    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberType(
            int page, int size, MemberType memberType) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findByMemberType(memberType, pageable);

        List<ResponseFindMemberVO> responseVOList = memberPage.getContent().stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        // nextCursor 설정
        Long nextCursor = null;
        if (!memberPage.getContent().isEmpty()) {
            nextCursor = memberPage.getContent()
                    .get(memberPage.getContent().size() - 1)
                    .getMemberCode();
        }
        log.info("{}: ", nextCursor);

        return new MemberPageResponse<>(
                responseVOList,
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.getNumber(),
                memberPage.getSize(),
                nextCursor
        );
    }

    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberCodeCursor(
            Long memberCodeCursor, int size, MemberType memberType) {
        PageRequest pageable = PageRequest.of(0, size);

        // 커서 기준으로 데이터 조회
        List<Member> members = memberRepository.findByMemberCodeLessThanAndMemberType(
                memberCodeCursor, memberType, pageable);
        log.info("members: {}", members);

        // 응답 데이터 매핑
        List<ResponseFindMemberVO> responseVOList = members.stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        // 다음 커서 설정
        Long nextCursor = responseVOList.isEmpty() ? null : members.get(members.size() - 1).getMemberCode();

        log.info("next는: {}", nextCursor);

        // memberType에 따른 전체 수 조회
        long totalElements = memberRepository.countByMemberType(memberType);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new MemberPageResponse<>(
                responseVOList,
                totalElements,
                totalPages,
                0,  // 커서 기반에서는 의미없음
                size,
                nextCursor
        );
    }

    public List<CategoryCountDTO> getCategoryCounts() {
        return lectureCategoryByLectureService.countLecturesByCategory();
    }

    public List<CategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return lectureCategoryByLectureService.countLecturesByCategoryWithinDateRange(startDate, endDate);
    }

    public Map<LectureCategoryEnum, Double> calculateCategoryPercentage(List<CategoryCountDTO> categoryCounts) {
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
