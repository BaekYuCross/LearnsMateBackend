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

    private String couponCode;
    private String couponName;
    private String couponContents;
    private int couponDiscountRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpireDate;
    private Boolean couponFlag;
    private int couponCategoryCode;
    private Long adminCode;
    private Long tutorCode;
}
