package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final CouponService couponService;
    private final MemberService memberService;
    private final LectureService lectureService;
    private final LectureCategoryByLectureService lectureCategoryByLectureService;
    private final PaymentRepository paymentRepository;
    private final IssueCouponService issueCouponService;

    // 직원이 전체 결제 내역을 조회
    public List<PaymentDetailDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new CommonException(StatusEnum.PAYMENT_NOT_FOUND);
        }

        return payments.stream()
                .map(payment -> {
                    // 강의 정보 가져오기
                    LectureDTO lectureDTO = lectureService.getLectureById(payment.getLectureByStudent().getLecture().getLectureCode());
                    String lectureTitle = lectureDTO.getLectureTitle();
                    Integer lecturePrice = lectureDTO.getLecturePrice();

                    List<String> lectureCategories = lectureCategoryByLectureService.findCategoryNamesByLectureCode(lectureDTO.getLectureCode());

                    Integer lectureClickCount = lectureDTO.getLectureClickCount();
                    LectureLevelEnum lectureLevel = lectureDTO.getLectureLevel();
                    Boolean lectureStatus = lectureDTO.getLectureStatus();

                    // 강사 정보 가져오기
                    MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
                    String tutorName = tutorDTO.getMemberName();
                    Long tutorCode = tutorDTO.getMemberCode();

                    // 학생 정보 가져오기
                    MemberDTO studentDTO = memberService.findMemberByMemberCode(payment.getLectureByStudent().getStudent().getMemberCode(), MemberType.STUDENT);
                    String studentName = studentDTO.getMemberName();
                    Long studentCode = studentDTO.getMemberCode();

                    String couponIssuanceCode = null;
                    String couponName = null;

                    if (couponIssuanceCode != null) {
                        couponIssuanceCode = payment.getCouponIssuance().getCouponIssuanceCode();
                        IssueCouponDTO issuedCoupon = issueCouponService.useIssuedCoupon(studentDTO.getMemberCode(), couponIssuanceCode);
                        if (issuedCoupon != null) {
                            CouponDTO coupon = couponService.findCouponByCouponCode(issuedCoupon.getCouponCode());
                            couponName = coupon.getCouponName();
                        }
                    }


                    // PaymentDetailDTO 빌더 패턴으로 변환
                    return PaymentDetailDTO.builder()
                            .paymentCode(payment.getPaymentCode())
                            .paymentPrice(payment.getPaymentPrice())
                            .createdAt(payment.getCreatedAt())
                            .lectureCode(payment.getLectureByStudent().getLecture().getLectureCode())
                            .lectureTitle(lectureTitle)
                            .lecturePrice(lecturePrice)
                            .tutorCode(tutorCode)
                            .tutorName(tutorName)
                            .studentCode(studentCode)
                            .studentName(studentName)
                            .lectureStatus(lectureStatus)
                            .lectureCategory(String.join(", ", lectureCategories)) // 카테고리를 쉼표로 구분하여 설정
                            .lectureClickCount(lectureClickCount)
                            .lectureLevel(lectureLevel)
                            .couponIssuanceCode(couponIssuanceCode)
                            .couponIssuanceName(couponName)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 단건 결제 내역 조회
    public PaymentDetailDTO getPaymentDetails(Long paymentCode) {
        // 결제 정보 조회
        Payment payment = paymentRepository.findById(paymentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));

        // 강의 정보 가져오기
        LectureDTO lectureDTO = lectureService.getLectureById(payment.getLectureByStudent().getLecture().getLectureCode());
        String lectureTitle = lectureDTO.getLectureTitle();
        Integer lecturePrice = lectureDTO.getLecturePrice();

        List<String> lectureCategories = lectureCategoryByLectureService.findCategoryNamesByLectureCode(lectureDTO.getLectureCode());
        Integer lectureClickCount = lectureDTO.getLectureClickCount();
        LectureLevelEnum lectureLevel = lectureDTO.getLectureLevel();
        Boolean lectureStatus = lectureDTO.getLectureStatus();

        // 강사 정보 가져오기
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        String tutorName = tutorDTO.getMemberName();
        Long tutorCode = tutorDTO.getMemberCode();

        // 학생 정보 가져오기
        MemberDTO studentDTO = memberService.findMemberByMemberCode(payment.getLectureByStudent().getStudent().getMemberCode(), MemberType.STUDENT);
        String studentName = studentDTO.getMemberName();
        Long studentCode = studentDTO.getMemberCode();

        // 쿠폰 정보 가져오기
        String couponIssuanceCode = null;
        String couponName = null;
        if (payment.getCouponIssuance() != null) {
            couponIssuanceCode = payment.getCouponIssuance().getCouponIssuanceCode();
            IssueCouponDTO issuedCoupon = issueCouponService.useIssuedCoupon(studentDTO.getMemberCode(), couponIssuanceCode);
            if (issuedCoupon != null) {
                CouponDTO coupon = couponService.findCouponByCouponCode(issuedCoupon.getCouponCode());
                couponName = coupon.getCouponName();
            }
        }

        // PaymentDetailDTO 빌더 패턴으로 변환하여 반환
        return PaymentDetailDTO.builder()
                .paymentCode(payment.getPaymentCode())
                .paymentPrice(payment.getPaymentPrice())
                .createdAt(payment.getCreatedAt())
                .lectureCode(payment.getLectureByStudent().getLecture().getLectureCode())
                .lectureTitle(lectureTitle)
                .lecturePrice(lecturePrice)
                .tutorCode(tutorCode)
                .tutorName(tutorName)
                .studentCode(studentCode)
                .studentName(studentName)
                .lectureStatus(lectureStatus)
                .lectureCategory(String.join(", ", lectureCategories)) // 카테고리를 쉼표로 구분하여 설정
                .lectureClickCount(lectureClickCount)
                .lectureLevel(lectureLevel)
                .couponIssuanceCode(couponIssuanceCode)
                .couponIssuanceName(couponName)
                .build();
    }
}
