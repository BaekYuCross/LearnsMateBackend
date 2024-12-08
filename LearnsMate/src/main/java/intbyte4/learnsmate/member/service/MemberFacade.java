package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.lecture_video_by_student.service.LectureVideoByStudentService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final LectureService lectureService;

    // 멤버 단일 조회시에 사용되는 서비스
    // memberCode로 학생 조회
    public FindSingleStudentDTO findStudentByStudentCode(Long studentCode) {
        Member member = memberRepository.findById(studentCode).orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));
        if (!member.getMemberType().equals(MemberType.STUDENT)) throw new CommonException(StatusEnum.ENUM_NOT_MATCH);

        // 0. 멤버 개인정보
        MemberDTO studentDTO = memberMapper.fromMembertoMemberDTO(member);

        // 1. 회원 보유중인 강의 정보
        List<LectureVideoProgressDTO> lectureVideoProgressDTOList = lectureVideoByStudentService.getVideoProgressByStudent(studentCode);

        // 2. 회원 쿠폰정보
        Map<String, List<IssueCouponDTO>> studentCoupons = issueCouponService.findAllStudentCoupons(studentCode);
        List<IssueCouponDTO> unusedCouponList = studentCoupons.get("unusedCoupons");
        List<IssueCouponDTO> usedCouponList = studentCoupons.get("usedCoupons");

        // 3. 회원 voc 정보
        List<VOCDTO> unansweredVOCByMemberList = vocService.findUnansweredVOCByMember(studentCode);
        List<VOCDTO> answeredVOCByMemberList = vocService.findAnsweredVOCByMember(studentCode);

        // 4. 강의 추천 로직 추가
        String latestLectureCode = paymentService.findLatestLectureCodeByStudent(studentCode);
        log.info("마지막 강의 코드: {}", latestLectureCode);

        List<Long> similarStudents = preferredTopicsService.findStudentsWithSimilarPreferredTopics(studentCode);
        log.info("비슷한 학생들은: {}", similarStudents);

        // 4. 강의 정보
        List<String> recommendedLectureCodes = Collections.emptyList();
        if (!similarStudents.isEmpty()) {
            Pageable pageable = PageRequest.of(0, 3); // 상위 3개만 가져오기
            List<Object[]> recommendedLectures = paymentService.findRecommendedLectures(similarStudents, latestLectureCode, studentCode, pageable);
            log.info("추천 강의들은: {}", recommendedLectures);
            recommendedLectureCodes = recommendedLectures.stream()
                    .map(record -> (String) record[0])
                    .collect(Collectors.toList());
        }
        List<LectureDTO> recommendedLectureList = new ArrayList<>();
        for (String lectureCode : recommendedLectureCodes) {
            recommendedLectureList.add(lectureService.getLectureById(lectureCode));
        }

        log.info("추천 강의 리스트는: {}", recommendedLectureList);

        return new FindSingleStudentDTO(
                studentDTO,
                lectureVideoProgressDTOList,
                unusedCouponList,
                usedCouponList,
                unansweredVOCByMemberList,
                answeredVOCByMemberList,
                recommendedLectureList
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

        return new MemberPageResponse<>(
                responseVOList,
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.getNumber(),
                memberPage.getSize()
        );
    }

    // sort 처리하기
    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberTypeBySort(
            int page, int size, MemberType memberType, String sortField, String sortDirection) {

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("DESC") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );

        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Member> memberPage = memberRepository.findByMemberTypeBySort(memberType, pageable);

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


    public List<CategoryCountDTO> getCategoryCounts() {
        return lectureCategoryByLectureService.countLecturesByCategory();
    }

    public List<CategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return lectureCategoryByLectureService.countLecturesByCategoryWithinDateRange(startDate, endDate);
    }

    // Facade에 메소드 추가
    public List<CategoryStatDTO> calculateCategoryStats(List<CategoryCountDTO> categoryCounts) {
        long total = categoryCounts.stream()
                .mapToLong(CategoryCountDTO::getCount)
                .sum();

        return categoryCounts.stream()
                .map(dto -> new CategoryStatDTO(
                        dto.getLectureCategoryName(),
                        dto.getCount(),
                        (double) dto.getCount() / total * 100
                ))
                .collect(Collectors.toList());
    }
}
