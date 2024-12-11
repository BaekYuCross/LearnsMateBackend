package intbyte4.learnsmate.coupon.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ResponseClientFindCouponVO {
    private String lectureCode;
    private String lectureTitle;
    private Integer lecturePrice;
    // 할인율, 할인 금액

    private Long couponCode;
    private Integer couponDiscountRate;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
    private Boolean activeState;
}
