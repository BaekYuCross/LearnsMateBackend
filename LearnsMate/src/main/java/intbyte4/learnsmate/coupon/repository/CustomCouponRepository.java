package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;

import java.util.List;

public interface CustomCouponRepository {

    List<CouponEntity> findCouponsByFilters(CouponFilterRequestVO request);
}
