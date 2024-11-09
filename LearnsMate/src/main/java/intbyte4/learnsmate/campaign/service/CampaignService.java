package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;

import java.util.List;

public interface CampaignService {
    CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);

    CampaignDTO editCampaign(CampaignDTO request, Long campaignCode);
    void removeCampaign(CampaignDTO request);
    List<CampaignDTO> findAllCampaigns();
    CampaignDTO findCampaign(CampaignDTO request);
}
