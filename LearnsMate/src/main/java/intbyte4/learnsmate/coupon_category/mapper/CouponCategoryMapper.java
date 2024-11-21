package intbyte4.learnsmate.coupon_category.mapper;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponCategoryMapper {

    public CouponCategoryDTO toDTO(CouponCategory entity) {
        return CouponCategoryDTO.builder()
                .couponCategoryCode(entity.getCouponCategoryCode())
                .couponCategoryName(entity.getCouponCategoryName())
                .build();
    }

    public CouponCategory toEntity(CouponCategoryDTO dto) {
        return CouponCategory.builder()
                .couponCategoryCode(dto.getCouponCategoryCode())
                .couponCategoryName(dto.getCouponCategoryName())
                .build();
    }
}
