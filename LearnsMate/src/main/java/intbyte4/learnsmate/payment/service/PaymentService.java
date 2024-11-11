package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    // 직원이 전체 결제 내역을 조회
    List<PaymentDTO> getAllPayments();

    // 직원이 특정 결제 내역을 단건 상세 조회
    PaymentDTO getPaymentDetails(Long paymentCode);

    PaymentDTO lecturePayment(MemberDTO memberDTO, LectureDTO lectureDTO, CouponDTO couponDTO);
}
