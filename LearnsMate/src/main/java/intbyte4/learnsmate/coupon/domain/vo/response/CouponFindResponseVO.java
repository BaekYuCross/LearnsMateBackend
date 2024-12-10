package intbyte4.learnsmate.coupon.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class CouponFindResponseVO {

    @JsonProperty("coupon_code")
    private Long couponCode;

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_contents")
    private String couponContents;

    @JsonProperty("coupon_discount_rate")
    private Integer couponDiscountRate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("coupon_start_date")
    private LocalDateTime couponStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("coupon_expire_date")
    private LocalDateTime couponExpireDate;

    @JsonProperty("coupon_flag")
    private Boolean couponFlag;

    @JsonProperty("active_state")
    private Boolean activeState;

    @JsonProperty("coupon_category_name")
    private String couponCategoryName;

    @JsonProperty("coupon_category_code")
    private Integer couponCategoryCode;

    @JsonProperty("admin_name")
    private String adminName;

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("tutor_name")
    private String tutorName;

    @JsonProperty("registration_type")
    private String registrationType;

    public CouponFindResponseVO updateAdminName(String adminName) {
        this.adminName = adminName;
        return this;
    }

    public CouponFindResponseVO updateTutorName(String tutorName) {
        this.tutorName = tutorName;
        return this;
    }
}
