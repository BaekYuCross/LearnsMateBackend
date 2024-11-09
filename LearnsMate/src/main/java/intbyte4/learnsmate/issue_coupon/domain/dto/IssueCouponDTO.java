package intbyte4.learnsmate.issue_coupon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class IssueCouponDTO {
    private int couponIssuanceCode;
    private LocalDateTime couponIssueDate;
    private Boolean couponUseStatus;
    private LocalDateTime couponUseDate;
    private Long studentCode;
    private Long couponCode;
}
