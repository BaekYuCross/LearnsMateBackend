package intbyte4.learnsmate.couponbycampaign.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CouponByCampaignDTO {
    private Long couponByCampaignCode;
    private Long couponCode;
    private Long campaignCode;
}
