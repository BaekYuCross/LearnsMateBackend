package intbyte4.learnsmate.issue_coupon.domain.vo.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IssueCouponFilterRequestVO {
    private String couponIssuanceCode;
    private String couponName;
    private String couponContents;
    private String couponCategoryName;
    private CouponCategory couponCategory;
    private Long studentCode;
    private String studentName;
    private Boolean couponUseStatus;
    private Integer couponDiscountRate;
    private LocalDateTime startCouponStartDate;
    private LocalDateTime endCouponStartDate;
    private LocalDateTime startCouponExpireDate;
    private LocalDateTime endCouponExpireDate;
    private LocalDateTime startCouponUseDate;
    private LocalDateTime endCouponUseDate;
    private LocalDateTime startCouponIssueDate;
    private LocalDateTime endCouponIssueDate;

    public Integer getCouponCategoryCode() {
        if (couponCategoryName != null && !couponCategoryName.isEmpty()) {
            try {
                return CouponCategoryEnum.getCodeByName(couponCategoryName);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 카테고리 이름입니다.");
            }
        }
        return null;
    }
}


