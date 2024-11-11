package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignService {
    CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);
    CampaignDTO editCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);
    void removeCampaign(CampaignDTO request);
    List<CampaignDTO> findAllCampaignList();
    CampaignDTO findCampaign(CampaignDTO request);
    List<CampaignDTO> findCampaignListByCondition
            (CampaignDTO request, LocalDateTime startDate, LocalDateTime endDate);
}
