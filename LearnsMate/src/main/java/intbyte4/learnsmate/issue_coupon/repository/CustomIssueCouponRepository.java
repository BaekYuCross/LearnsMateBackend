package intbyte4.learnsmate.issue_coupon.repository;


import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;

import java.util.List;

public interface CustomIssueCouponRepository {
    List<IssueCoupon> findIssuedCouponsByFilters(IssueCouponFilterRequestVO request);
}
