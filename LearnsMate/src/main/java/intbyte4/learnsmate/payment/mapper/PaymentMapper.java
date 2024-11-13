package intbyte4.learnsmate.payment.mapper;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.RequestRegisterPaymentVO;
import intbyte4.learnsmate.payment.domain.vo.ResponseFindPaymentVO;
import intbyte4.learnsmate.payment.domain.vo.ResponseRegisterPaymentVO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    // PaymentDTO -> Payment
    public Payment toEntity(PaymentDTO paymentDTO, LectureByStudent lectureByStudent, IssueCoupon issueCoupon) {
        return Payment.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureByStudent(lectureByStudent)
                .couponIssuance(issueCoupon)
                .build();
    }

    // Payment -> PaymentDTO
    public PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentCode(payment.getPaymentCode())
                .paymentPrice(payment.getPaymentPrice())
                .createdAt(payment.getCreatedAt())
                .lectureByStudentCode(payment.getLectureByStudent().getLectureByStudentCode())
                .couponIssuanceCode(payment.getCouponIssuance().getCouponIssuanceCode())
                .build();
    }

    // PaymentDTO -> PaymentResponseVO
    public ResponseFindPaymentVO fromDtoToResponseVO(PaymentDTO paymentDTO) {
        return ResponseFindPaymentVO.builder()
                .paymentCode(paymentDTO.getPaymentCode())
                .paymentPrice(paymentDTO.getPaymentPrice())
                .createdAt(paymentDTO.getCreatedAt())
                .lectureByStudentCode(paymentDTO.getLectureByStudentCode())
                .couponIssuanceCode(paymentDTO.getCouponIssuanceCode())
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
