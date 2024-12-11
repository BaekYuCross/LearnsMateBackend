package intbyte4.learnsmate.issue_coupon.domain.dto;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class IssuedCouponFilterDTO {
    private String couponIssuanceCode;
    private String couponName;
    private String couponContents;
    private String couponCategoryName;
    private CouponCategory couponCategory;
    private Long studentCode;
    private String studentName;
    private Boolean couponUseStatus;
    private Integer minDiscountRate;
    private Integer maxDiscountRate;
    private LocalDateTime startCouponStartDate;
    private LocalDateTime endCouponStartDate;
    private LocalDateTime startCouponExpireDate;
    private LocalDateTime endCouponExpireDate;
    private LocalDateTime startCouponUseDate;
    private LocalDateTime endCouponUseDate;
    private LocalDateTime startCouponIssueDate;
    private LocalDateTime endCouponIssueDate;

    private List<String> selectedColumns;
}
