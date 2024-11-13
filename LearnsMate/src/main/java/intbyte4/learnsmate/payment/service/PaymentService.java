package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> lecturePayment(MemberDTO memberDTO, List<LectureDTO> lectureDTOList, IssueCouponDTO issueCouponDTO);
}
