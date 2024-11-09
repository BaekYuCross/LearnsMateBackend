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
public class IssueCouponUseResponseVO {

    @JsonProperty("coupon_issuance_code")
    private String couponIssuanceCode;

    @JsonProperty("coupon_use_status")
    private Boolean couponUseStatus;

    @JsonProperty("coupon_use_date")
    private LocalDateTime couponUseDate;
}