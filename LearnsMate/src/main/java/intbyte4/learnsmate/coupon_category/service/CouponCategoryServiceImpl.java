package intbyte4.learnsmate.coupon_category.service;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.repository.CouponCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("couponCategoryService")
@RequiredArgsConstructor
public class CouponCategoryServiceImpl implements CouponCategoryService {
    private final CouponCategoryRepository couponCategoryRepository;

    @Override
    public CouponCategory findByCouponCategoryCode(Integer couponCategoryCode) {
        log.info("vocCategory 조회 중: {}", couponCategoryCode);
        return couponCategoryRepository.findById(couponCategoryCode).orElseThrow(() -> new RuntimeException("couponCategoryCode not found"));
    }
}
