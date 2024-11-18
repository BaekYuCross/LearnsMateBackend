package intbyte4.learnsmate.issue_coupon.repository;

import intbyte4.learnsmate.issue_coupon.domain.dto.AllIssuedCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;

import java.util.List;

public interface CustomIssueCouponRepository {
    List<AllIssuedCouponDTO> findIssuedCouponsByFilters(IssueCouponFilterRequestVO request);
}
