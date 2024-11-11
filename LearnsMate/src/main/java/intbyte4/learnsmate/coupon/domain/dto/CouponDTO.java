package intbyte4.learnsmate.coupon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CouponDTO {

    private Long couponCode;
    private String couponName;
    private String couponContents;
    private Integer couponDiscountRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
    private Boolean couponFlag;
    private Boolean activeState;
    private Integer couponCategoryCode;
    private Long adminCode;
    private Long tutorCode;
}
