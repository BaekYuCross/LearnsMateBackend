package intbyte4.learnsmate.campaign.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestEditCampaignCouponVO {
    @JsonProperty("coupon_code")
    private String couponCode;

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_contents")
    private String couponContents;

    @JsonProperty("coupon_discount_rate")
    private int couponDiscountRate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("coupon_start_date")
    private LocalDateTime couponStartDate;

    @JsonProperty("coupon_expire_date")
    private LocalDateTime couponExpireDate;

    @JsonProperty("coupon_flag")
    private Boolean couponFlag;

    @JsonProperty("coupon_category_code")
    private int couponCategoryCode;

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("tutor_code")
    private Long tutorCode;
}
