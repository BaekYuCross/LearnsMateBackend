package intbyte4.learnsmate.coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TutorCouponRegisterRequestVO {
    private Long couponCode;

    private String couponName;

    private Integer couponDiscountRate;

    private LocalDateTime couponExpireDate;

    private Long tutorCode;

    private Integer couponCategoryCode;

    private String lectureCode;
}
