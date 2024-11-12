package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberIssueCouponDTO {

    List<IssueCouponDTO> unusedCoupons;
    List<IssueCouponDTO> usedCoupons;
}
