package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAllPayments();

    List<PaymentFilterDTO> getPaymentsByFilters(PaymentFilterRequestVO request);

    PaymentDTO getPaymentDetails(Long paymentCode);

    PaymentDTO lectureAdaptedPayment(MemberDTO memberDTO, LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO);

    PaymentDTO lectureUnAdaptedPayment(MemberDTO memberDTO, LectureDTO lectureDTO);

    int getPurchaseCountByLectureCode(String lectureCode);
}
