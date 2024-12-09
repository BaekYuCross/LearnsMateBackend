package intbyte4.learnsmate.issue_coupon.repository;


import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CustomIssueCouponRepository {
    List<IssueCoupon> findIssuedCouponsByFilters(IssueCouponFilterRequestVO request);

    // 발급 쿠폰 offset 페이지네이션 필터링
    Page<IssueCoupon> getFilteredIssuedCoupons(IssueCouponFilterRequestVO request, PageRequest pageable);

    // 필터링x 정렬o
    Page<IssueCoupon> findAllByOrderByCouponIssueDateDescWithSort(PageRequest pageable);
    // 필터링o 정렬o
    Page<IssueCoupon> getFilteredIssuedCouponsWithSort(IssueCouponFilterRequestVO request, PageRequest pageable);
}
