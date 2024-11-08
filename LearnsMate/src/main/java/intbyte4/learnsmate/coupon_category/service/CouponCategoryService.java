package intbyte4.learnsmate.coupon_category.service;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;

public interface CouponCategoryService {
    CouponCategory findByCouponCategoryCode(Integer couponCategoryCode);
}
