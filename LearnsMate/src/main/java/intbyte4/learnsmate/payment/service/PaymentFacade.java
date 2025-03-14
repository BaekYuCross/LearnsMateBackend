package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.PaymentPageResponse;
import intbyte4.learnsmate.payment.domain.vo.ResponseFindPaymentVO;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final CouponService couponService;
    private final MemberService memberService;
    private final LectureService lectureService;
    private final LectureCategoryByLectureService lectureCategoryByLectureService;
    private final PaymentRepository paymentRepository;
    private final IssueCouponService issueCouponService;
    private final PaymentMapper paymentMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> getPaymentsWithGraph(int page, int size) {
        Page<Payment> payments = getPaymentsWithPagination(page, size);
        Map<Integer, List<PaymentMonthlyRevenueDTO>> graphData = (page == 0) ? getMonthlyRevenueComparison() : null;

        List<ResponseFindPaymentVO> paymentVOs = payments.stream()
                .map(this::getPaymentDetailDTO)
                .map(paymentMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());

        boolean hasNext = payments.hasNext();
        long totalElements = payments.getTotalElements();

        return new PaymentPageResponse<>(paymentVOs, graphData, hasNext, totalElements);
    }

    // Facade
    public PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> getPaymentsWithGraphAndSort(
            int page, int size, String sortField, String sortDirection) {

        String redisKey = "payments:page=" + page + ":size=" + size + ":sort=" + sortField + "_" + sortDirection;

        log.info("🔍 Redis에서 캐시 확인: {}", redisKey);
        long startTime = System.currentTimeMillis();

        // *️⃣ Redis에서 데이터 조회 (캐시 확인)
        PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> cachedData =
                (PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>>) redisTemplate.opsForValue().get(redisKey);

        if (cachedData != null) {
            log.info("✅ Redis 캐시 HIT! 캐싱된 데이터 반환");
            long endTime = System.currentTimeMillis();
            log.info("🕒 캐시 데이터 조회 시간: {} ms", (endTime - startTime));
            return cachedData;
        }

        log.info("🚨 Redis 캐시 MISS! DB에서 조회 시작");

        // 2️⃣ 기존 데이터 조회 로직
        log.info("getPaymentsWithPaginationAndSort2 시작");
        startTime = System.currentTimeMillis();
        Page<PaymentDetailDTO> paymentDetailDTOs = getPaymentsWithPaginationAndSort2(page, size, sortField, sortDirection);
        long endTime = System.currentTimeMillis();
        log.info("getPaymentsWithPaginationAndSort2 종료 | 실행 시간: {} ms", (endTime - startTime));

        log.info("getMonthlyRevenueComparisonWithSort 시작");
        Map<Integer, List<PaymentMonthlyRevenueDTO>> graphData = (page == 0) ? getMonthlyRevenueComparisonWithSort() : null;
        log.info("getMonthlyRevenueComparisonWithSort 종료");

        List<ResponseFindPaymentVO> paymentVOs = paymentDetailDTOs.stream()
                .map(paymentMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());

        boolean hasNext = paymentDetailDTOs.hasNext();
        long totalElements = paymentDetailDTOs.getTotalElements();

        // 3️⃣ Redis에 저장 (TTL 30분)
        PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> response =
                new PaymentPageResponse<>(paymentVOs, graphData, hasNext, totalElements);

        redisTemplate.opsForValue().set(redisKey, response, Duration.ofMinutes(30));
        log.info("📌 Redis에 데이터 저장 완료 (TTL: 30분)");

        return response;
    }


    // 정렬
    private Page<Payment> getPaymentsWithPaginationAndSort(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepository.findAllWithSort(pageable);
    }

    // 정렬
    private Page<PaymentDetailDTO> getPaymentsWithPaginationAndSort2(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepository.findAllWithSort2(pageable);
    }


    // 정렬
    private String getSortFieldName(String sortField) {
        return switch (sortField.toLowerCase()) {
            case "membercode", "member_code" -> "student.memberCode";
            case "payment_price", "paymentprice" -> "paymentPrice";
            case "created_at", "createdat" -> "createdAt";
            case "lecture_code", "lecturecode" -> "lecture.lectureCode";
            case "lecture_title", "lecturetitle" -> "lecture.title";
            case "lecture_price", "lectureprice" -> "lecture.price";
            case "tutor_code", "tutorcode" -> "lecture.tutor.tutorCode";
            case "tutor_name", "tutorname" -> "lecture.tutor.name";
            case "student_code", "studentcode" -> "student.studentCode";
            case "student_name", "studentname" -> "student.name";
            case "lecture_status", "lecturestatus" -> "lecture.status";
            case "lecture_category", "lecturecategory" -> "lecture.category";
            case "lecture_click_count", "lectureclickcount" -> "lecture.clickCount";
            case "lecture_level", "lecturelevel" -> "lecture.level";
            default -> "createdAt";
        };
    }

    // 정렬
    private Map<Integer, List<PaymentMonthlyRevenueDTO>> getMonthlyRevenueComparisonWithSort() {
        int currentYear = LocalDateTime.now(ZoneId.of("Asia/Seoul")).getYear();
        int previousYear = currentYear - 1;

        List<PaymentMonthlyRevenueDTO> revenues = paymentRepository.findMonthlyRevenueWithComparison(currentYear, previousYear);

        return revenues.stream()
                .collect(Collectors.groupingBy(PaymentMonthlyRevenueDTO::getYear));
    }

    private Page<Payment> getPaymentsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return paymentRepository.findAll(pageable);
    }

    private Map<Integer, List<PaymentMonthlyRevenueDTO>> getMonthlyRevenueComparison() {
        int currentYear = LocalDateTime.now(ZoneId.of("Asia/Seoul")).getYear();
        int previousYear = currentYear - 1;

        List<PaymentMonthlyRevenueDTO> revenues = paymentRepository.findMonthlyRevenueWithComparison(currentYear, previousYear);

        return revenues.stream()
                .collect(Collectors.groupingBy(PaymentMonthlyRevenueDTO::getYear));
    }

    public PaymentDetailDTO getPaymentDetails(Long paymentCode) {
        Payment payment = paymentRepository.findByPaymentCode(paymentCode).orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));
        return getPaymentDetailDTO(payment);
    }

    private PaymentDetailDTO getPaymentDetailDTO(Payment payment) {
        LectureDTO lectureDTO = lectureService.getLectureById(payment.getLectureByStudent().getLecture().getLectureCode());
        List<String> lectureCategories = lectureCategoryByLectureService.findCategoryNamesByLectureCode(lectureDTO.getLectureCode());
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        MemberDTO studentDTO = memberService.findMemberByMemberCode(payment.getLectureByStudent().getStudent().getMemberCode(), MemberType.STUDENT);

        String couponIssuanceCode = null;
        String couponName = null;

        if (payment.getCouponIssuance() != null) {
            couponIssuanceCode = payment.getCouponIssuance().getCouponIssuanceCode();
            if (couponIssuanceCode != null) {
                IssueCouponDTO issuedCoupon = issueCouponService.useIssuedCoupon(studentDTO.getMemberCode(), couponIssuanceCode);
                if (issuedCoupon != null) {
                    CouponDTO coupon = couponService.findCouponDTOByCouponCode(issuedCoupon.getCouponCode());
                    couponName = coupon.getCouponName();
                }
            }
        }

        return buildPaymentDetailDTO(payment, lectureDTO, tutorDTO, studentDTO, lectureCategories, couponIssuanceCode, couponName);
    }

    private PaymentDetailDTO buildPaymentDetailDTO(Payment payment, LectureDTO lectureDTO, MemberDTO tutorDTO, MemberDTO studentDTO, List<String> lectureCategories, String couponIssuanceCode, String couponName) {
        return PaymentDetailDTO.builder()
                .paymentCode(payment.getPaymentCode())
                .paymentPrice(payment.getPaymentPrice())
                .createdAt(payment.getCreatedAt())
                .lectureCode(payment.getLectureByStudent().getLecture().getLectureCode())
                .lectureTitle(lectureDTO.getLectureTitle())
                .lecturePrice(lectureDTO.getLecturePrice())
                .lectureStatus(lectureDTO.getLectureStatus())
                .tutorCode(tutorDTO.getMemberCode())
                .tutorName(tutorDTO.getMemberName())
                .studentCode(studentDTO.getMemberCode())
                .studentName(studentDTO.getMemberName())
                .lectureCategory(String.join(", ", lectureCategories))
                .lectureLevel(LectureLevelEnum.valueOf(lectureDTO.getLectureLevel()))
                .lectureClickCount(lectureDTO.getLectureClickCount())
                .couponIssuanceCode(couponIssuanceCode)
                .couponIssuanceName(couponName)
                .build();
    }
}
