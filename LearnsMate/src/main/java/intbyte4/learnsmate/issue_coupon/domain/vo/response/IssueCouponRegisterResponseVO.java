package intbyte4.learnsmate.issue_coupon.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCouponRegisterResponseVO {

    @JsonProperty("coupon_issuance_code")
    private int couponIssuanceCode;

    @JsonProperty("coupon_issue_date")
    private LocalDateTime couponIssueDate;

    @JsonProperty("coupon_use_status")
    private Boolean couponUseStatus;

    @JsonProperty("coupon_use_date")
    private LocalDateTime couponUseDate;

    @JsonProperty("student_code")
    private Long studentCode;

    @JsonProperty("coupon_code")
    private Long couponCode;
}
