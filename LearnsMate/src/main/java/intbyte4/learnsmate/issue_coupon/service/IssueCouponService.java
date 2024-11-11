package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IssueCouponService {
    List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request);

    List<IssueCouponDTO> findIssuedCouponsByStudent(Long studentCode);

    IssueCouponDTO useIssuedCoupon(Long studentCode, String couponIssuanceCode);

    // 보유중인 쿠폰 조회
    @Transactional
    List<IssueCouponDTO> findAllStudentCoupons(IssueCouponDTO dto, Long studentCode);

    // 사용한 쿠폰 조회
    @Transactional
    List<IssueCouponDTO> findAllUsedStudentCoupons(IssueCouponDTO dto, Long studentCode);
}
