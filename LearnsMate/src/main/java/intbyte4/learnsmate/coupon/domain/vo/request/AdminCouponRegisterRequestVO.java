package intbyte4.learnsmate.coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminCouponRegisterRequestVO {
    private String couponName;
    private String couponCategoryName;
    private String couponContents;
    private Integer couponDiscountRate;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
}
