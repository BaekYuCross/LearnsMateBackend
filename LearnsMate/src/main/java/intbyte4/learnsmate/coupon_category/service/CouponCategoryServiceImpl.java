package intbyte4.learnsmate.coupon_category.service;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryDTO;
import intbyte4.learnsmate.coupon_category.mapper.CouponCategoryMapper;
import intbyte4.learnsmate.coupon_category.repository.CouponCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("couponCategoryService")
@RequiredArgsConstructor
public class CouponCategoryServiceImpl implements CouponCategoryService {
    private final CouponCategoryRepository couponCategoryRepository;
    private final CouponCategoryMapper couponCategoryMapper;

    @Override
    public CouponCategory findByCouponCategoryCode(Integer couponCategoryCode) {
        return couponCategoryRepository.findById(couponCategoryCode).orElseThrow(() -> new RuntimeException("couponCategoryCode not found"));
    }

    @Override
    public CouponCategoryDTO findDTOByCouponCategoryCode(Integer couponCategoryCode) {
        CouponCategory couponCategory = couponCategoryRepository
                .findById(couponCategoryCode).orElseThrow(() -> new RuntimeException("couponCategoryCode not found"));
        return couponCategoryMapper.toDTO(couponCategory);
    }

    @Override
    public CouponCategory findCouponCategoryByName(String couponCategoryName) {
        log.info("{}", couponCategoryName);
        return couponCategoryRepository.findCouponCategoryByCouponCategoryName(couponCategoryName);
    }
}
