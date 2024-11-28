package intbyte4.learnsmate.coupon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CouponFilterDTO {
    private String couponName;
    private String couponContents;
    private Boolean activeState;
    private LocalDateTime startExpireDate;
    private LocalDateTime endExpireDate;
    private LocalDateTime startCreatedAt;
    private LocalDateTime endCreatedAt;
    private Integer minDiscountRate;
    private Integer maxDiscountRate;
    private LocalDateTime startCouponStartDate;
    private LocalDateTime endCouponStartDate;
    private String couponCategoryName;
    private String adminName;
    private String tutorName;

    private List<String> selectedColumns;
}
