package intbyte4.learnsmate.coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TutorCouponEditRequestVO {

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_discount_rate")
    private Integer couponDiscountRate;

    @JsonProperty("coupon_start_date")
    private LocalDateTime couponStartDate;

    @JsonProperty("coupon_expire_date")
    private LocalDateTime couponExpireDate;

    @JsonProperty("coupon_category_code")
    private Integer couponCategoryCode;
}
