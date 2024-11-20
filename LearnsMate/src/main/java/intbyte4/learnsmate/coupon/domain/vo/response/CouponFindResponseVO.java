package intbyte4.learnsmate.coupon.domain.vo.response;

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
public class CouponFindResponseVO {

    @JsonProperty("coupon_code")
    private Long couponCode;

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_contents")
    private String couponContents;

    @JsonProperty("coupon_discount_rate")
    private Integer couponDiscountRate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("coupon_start_date")
    private LocalDateTime couponStartDate;

    @JsonProperty("coupon_expire_date")
    private LocalDateTime couponExpireDate;

    @JsonProperty("active_state")
    private Boolean activeState;

    @JsonProperty("coupon_category_code")
    private Integer couponCategoryCode;

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("tutor_code")
    private Long tutorCode;

}
