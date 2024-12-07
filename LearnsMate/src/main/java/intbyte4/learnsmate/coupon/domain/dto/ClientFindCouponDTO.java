package intbyte4.learnsmate.coupon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ClientFindCouponDTO {
    private String lectureCode;
    private String lectureTitle;
    private Integer lecturePrice;
    private Long couponCode;
    private Integer couponDiscountRate;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
    private Boolean activeState;
}
