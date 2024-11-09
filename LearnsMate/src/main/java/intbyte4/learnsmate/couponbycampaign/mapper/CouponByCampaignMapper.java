package intbyte4.learnsmate.couponbycampaign.mapper;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponByCampaignMapper {

    public CouponByCampaignDTO toDTO(CouponByCampaign entity) {
        return CouponByCampaignDTO.builder()
                .couponByCampaignCode(entity.getCouponByCampaignCode())
                .CouponCode(entity.getCoupon().getCouponCode())
                .CampaignCode(entity.getCampaign().getCampaignCode())
                .build();
    }

    public CouponByCampaign toEntity(CouponByCampaignDTO dto, CouponEntity coupon, Campaign campaign) {
        return CouponByCampaign.builder()
                .couponByCampaignCode(dto.getCouponByCampaignCode())
                .coupon(coupon)
                .campaign(campaign)
                .build();
    }
}
