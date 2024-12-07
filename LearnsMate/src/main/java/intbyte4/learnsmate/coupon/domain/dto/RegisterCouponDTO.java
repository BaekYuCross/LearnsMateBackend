package intbyte4.learnsmate.coupon.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterCouponDTO {

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
    private String couponCategoryName;
    private Long adminCode;
    private Long tutorCode;
}
