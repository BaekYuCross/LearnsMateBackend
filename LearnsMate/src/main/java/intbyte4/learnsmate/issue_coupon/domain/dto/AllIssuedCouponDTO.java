package intbyte4.learnsmate.issue_coupon.domain.dto;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AllIssuedCouponDTO {
    private String couponIssuanceCode;
    private String couponName;
    private String couponContents;
    private Integer couponCategoryCode;
    private Long studentCode;
    private String studentName;
    private Boolean couponUseStatus;
    private Integer couponDiscountRate;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
    private LocalDateTime couponUseDate;
    private LocalDateTime couponIssueDate;
}
