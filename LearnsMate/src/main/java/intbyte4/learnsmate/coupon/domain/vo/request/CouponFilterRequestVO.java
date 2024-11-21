package intbyte4.learnsmate.coupon.domain.vo.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CouponFilterRequestVO {

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_contents")
    private String couponContents;

    @JsonProperty("active_state")
    private Boolean activeState;

    @JsonProperty("start_expire_date")
    private LocalDateTime startExpireDate;

    @JsonProperty("end_expire_date")
    private LocalDateTime endExpireDate;

    @JsonProperty("start_created_at")
    private LocalDateTime startCreatedAt;

    @JsonProperty("end_created_at")
    private LocalDateTime endCreatedAt;

    @JsonProperty("min_discount_rate")
    private Integer minDiscountRate;

    @JsonProperty("max_discount_rate")
    private Integer maxDiscountRate;

    @JsonProperty("start_coupon_start_date")
    private LocalDateTime startCouponStartDate;

    @JsonProperty("end_coupon_start_date")
    private LocalDateTime endCouponStartDate;

    @JsonProperty("coupon_category_name")
    private String couponCategoryName;

    @JsonProperty("admin_name")
    private String adminName;

    @JsonProperty("tutor_name")
    private String tutorName;
}
