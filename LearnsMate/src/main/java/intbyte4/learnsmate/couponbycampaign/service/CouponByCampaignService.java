package intbyte4.learnsmate.couponbycampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;

import java.util.List;

public interface CouponByCampaignService {
    void registerCouponByCampaign(CouponDTO coupon, CampaignDTO campaign);
    List<CouponByCampaignDTO> findByCampaignCode(Campaign updatedCampaign);
    void removeCouponByCampaign(Long couponByCampaignCode);
    void removeByCampaignCode(Long campaignCode);
}
    