package intbyte4.learnsmate.coupon_category.service;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryDTO;

public interface CouponCategoryService {
    CouponCategory findByCouponCategoryCode(Integer couponCategoryCode);

    CouponCategoryDTO findDTOByCouponCategoryCode(Integer couponCategoryCode);
}
