package intbyte4.learnsmate.couponbycampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;
import intbyte4.learnsmate.couponbycampaign.repository.CouponByCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponByCampaignServiceImpl implements CouponByCampaignService{
    private final CouponByCampaignRepository couponByCampaignRepository;

    @Override
    public void registerCouponByCampaign(CouponDTO coupon, CampaignDTO campaign) {
        CouponEntity getCouponCode = CouponEntity.builder()
                .couponCode(coupon.getCouponCode())
                .build();

        Campaign getCampaignCode = Campaign.builder()
                .campaignCode(campaign.getCampaignCode())
                .build();

        CouponByCampaign couponByCampaign = CouponByCampaign.builder()
                .couponByCampaignCode(null)
                .coupon(getCouponCode)
                .campaign(getCampaignCode)
                .build();

        couponByCampaignRepository.save(couponByCampaign);
    }
}
