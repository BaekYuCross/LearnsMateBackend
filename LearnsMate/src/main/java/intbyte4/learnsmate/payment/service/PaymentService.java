package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.util.List;

public interface PaymentService {
    // 직원이 전체 결제 내역을 조회
    List<PaymentDTO> getAllPayments();

    List<PaymentFilterDTO> getPaymentsByFilters(PaymentFilterRequestVO request);

    // 직원이 특정 결제 내역을 단건 상세 조회
    PaymentDTO getPaymentDetails(Long paymentCode);

    List<PaymentDTO> lecturePayment(MemberDTO memberDTO, List<LectureDTO> lectureDTOList, List<IssueCouponDTO> issueCouponDTOList);
}
