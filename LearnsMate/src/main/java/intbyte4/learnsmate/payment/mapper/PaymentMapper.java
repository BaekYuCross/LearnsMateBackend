package intbyte4.learnsmate.payment.mapper;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.ResponseFindPaymentVO;
import intbyte4.learnsmate.payment.domain.vo.ResponseRegisterPaymentVO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    // PaymentDTO -> Payment
    public Payment toAdaptEntity(PaymentDTO paymentDTO, LectureByStudent lectureByStudent, IssueCoupon issueCoupon) {
        return Payment.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureByStudent(lectureByStudent)
                .couponIssuance(issueCoupon)
                .build();
    }

    public Payment toEntity(PaymentDTO paymentDTO, LectureByStudent lectureByStudent) {
        return Payment.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureByStudent(lectureByStudent)
                .build();
    }

    // Payment -> PaymentDTO
    public PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentCode(payment.getPaymentCode())
                .paymentPrice(payment.getPaymentPrice())
                .createdAt(payment.getCreatedAt())
                .lectureByStudentCode(payment.getLectureByStudent().getLectureByStudentCode())
                .couponIssuanceCode(
                        payment.getCouponIssuance() != null ? payment.getCouponIssuance().getCouponIssuanceCode() : null
                )
                .build();
    }

    // PaymentDTO -> PaymentResponseVO
    public ResponseFindPaymentVO fromDtoToResponseVO(PaymentDetailDTO paymentDTO) {
        return ResponseFindPaymentVO.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureCode(paymentDTO.getLectureCode())
                .lectureTitle(paymentDTO.getLectureTitle())
                .lecturePrice(paymentDTO.getLecturePrice())
                .tutorCode(paymentDTO.getTutorCode())
                .tutorName(paymentDTO.getTutorName())
                .studentCode(paymentDTO.getStudentCode())
                .studentName(paymentDTO.getStudentName())
                .lectureStatus(paymentDTO.getLectureStatus())
                .lectureCategory(paymentDTO.getLectureCategory())
                .lectureClickCount(paymentDTO.getLectureClickCount())
                .lectureLevel(paymentDTO.getLectureLevel())
                .couponIssuanceCode(paymentDTO.getCouponIssuanceCode())
                .couponIssuanceName(paymentDTO.getCouponIssuanceName())
                .build();
    }


    // PaymentDTO -> PaymentResponseVO
    public ResponseFindPaymentVO fromFilterDtoToResponseVO(PaymentFilterDTO paymentDTO) {
        return ResponseFindPaymentVO.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureCode(paymentDTO.getLectureCode())
                .lectureTitle(paymentDTO.getLectureTitle())
                .lecturePrice(paymentDTO.getLecturePrice())
                .tutorCode(paymentDTO.getTutorCode())
                .tutorName(paymentDTO.getTutorName())
                .studentCode(paymentDTO.getStudentCode())
                .studentName(paymentDTO.getStudentName())
                .lectureCategory(paymentDTO.getLectureCategoryName())
                .couponIssuanceCode(paymentDTO.getCouponIssuanceCode())
                .couponIssuanceName(paymentDTO.getCouponIssuanceName())
                .build();
    }


    public ResponseRegisterPaymentVO fromPaymentDTOtoResponseRegisterPaymentVO(PaymentDTO dto) {
        return ResponseRegisterPaymentVO.builder()
                .paymentCode(dto.getPaymentCode())
                .paymentPrice(dto.getPaymentPrice())
                .createdAt(dto.getCreatedAt())
                .lectureByStudentCode(dto.getLectureByStudentCode())
                .couponIssuanceCode(dto.getCouponIssuanceCode())
                .build();
    }
}
