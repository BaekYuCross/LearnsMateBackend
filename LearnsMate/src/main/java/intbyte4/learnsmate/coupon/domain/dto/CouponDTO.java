package intbyte4.learnsmate.coupon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CouponDTO {

    private Long couponCode;
    private String couponName;
    private String couponContents;
    private int couponDiscountRate;
    private String couponType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean couponFlag;
    private Long campaignCode;
    private Long adminCode;
    private Long tutorCode;
}
