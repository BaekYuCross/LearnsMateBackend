package intbyte4.learnsmate.coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminCouponRegisterRequestVO {

//    @JsonProperty("lecture_list")
    private List<String> lectureCode;

//    @JsonProperty("coupon_name")
    private String couponName;

//    @JsonProperty("coupon_category_name")
    private String couponCategoryName;

//    @JsonProperty("coupon_contents")
    private String couponContents;

//    @JsonProperty("coupon_discount_rate")
    private Integer couponDiscountRate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    @JsonProperty("coupon_start_date")
    private LocalDateTime couponStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    @JsonProperty("coupon_expire_date")
    private LocalDateTime couponExpireDate;

//    @JsonProperty("admin_code")
    private Long adminCode;
}
