package intbyte4.learnsmate.coupon_category.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CouponCategoryDTO {
    private Integer couponCategoryCode;
    private String couponCategoryName;
}
