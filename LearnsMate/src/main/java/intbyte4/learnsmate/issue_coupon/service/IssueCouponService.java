package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;

import java.util.List;

public interface IssueCouponService {
    List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request);

    List<IssueCouponDTO> findIssuedCouponsByStudent(Long studentCode);
}
