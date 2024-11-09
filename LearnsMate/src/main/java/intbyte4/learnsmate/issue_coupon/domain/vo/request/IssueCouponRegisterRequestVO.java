package intbyte4.learnsmate.issue_coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class IssueCouponRegisterRequestVO {

    @JsonProperty("student_codes")
    private List<Long> studentCodes;

    @JsonProperty("coupon_codes")
    private List<Long> couponCodes;
}
