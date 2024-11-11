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

    @JsonProperty("coupon_flag")
    private Boolean couponFlag;

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

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("start_coupon_start_date")
    private LocalDateTime startCouponStartDate;

    @JsonProperty("end_coupon_start_date")
    private LocalDateTime endCouponStartDate;

    @JsonProperty("coupon_category_code")
    private Integer couponCategoryCode;

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("tutor_code")
    private Long tutorCode;
}
