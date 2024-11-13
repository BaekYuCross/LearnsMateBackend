package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
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

    public List<PaymentDetailDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new CommonException(StatusEnum.PAYMENT_NOT_FOUND);
        }

        return payments.stream()
                .map(this::getPaymentDetailDTO)
                .collect(Collectors.toList());
    }

    public PaymentDetailDTO getPaymentDetails(Long paymentCode) {
        Payment payment = paymentRepository.findById(paymentCode).orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));
        return getPaymentDetailDTO(payment);
    }

    private PaymentDetailDTO getPaymentDetailDTO(Payment payment) {
        LectureDTO lectureDTO = lectureService.getLectureById(payment.getLectureByStudent().getLecture().getLectureCode());
        List<String> lectureCategories = lectureCategoryByLectureService.findCategoryNamesByLectureCode(lectureDTO.getLectureCode());
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        MemberDTO studentDTO = memberService.findMemberByMemberCode(payment.getLectureByStudent().getStudent().getMemberCode(), MemberType.STUDENT);

        String couponIssuanceCode = payment.getCouponIssuance().getCouponIssuanceCode();
        String couponName = null;

        if (couponIssuanceCode != null) {
            IssueCouponDTO issuedCoupon = issueCouponService.useIssuedCoupon(studentDTO.getMemberCode(), couponIssuanceCode);
            if (issuedCoupon != null) {
                CouponDTO coupon = couponService.findCouponDTOByCouponCode(issuedCoupon.getCouponCode());
                couponName = coupon.getCouponName();
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
                .tutorCode(tutorDTO.getMemberCode())
                .tutorName(tutorDTO.getMemberName())
                .studentCode(studentDTO.getMemberCode())
                .studentName(studentDTO.getMemberName())
                .lectureCategory(String.join(", ", lectureCategories))
                .couponIssuanceCode(couponIssuanceCode)
                .couponIssuanceName(couponName)
                .build();
    }
}
