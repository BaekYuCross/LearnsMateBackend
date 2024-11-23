package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.*;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByConditionVO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;

import java.util.List;

public interface CampaignService {
    CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);
    CampaignDTO editCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);
    void removeCampaign(CampaignDTO request);
    List<FindAllCampaignsDTO> findAllCampaignList();
    FindCampaignDTO findCampaign(FindCampaignDTO request);
    CampaignPageResponse<ResponseFindCampaignByConditionVO> findCampaignListByCondition
            (CampaignFilterDTO request, int page, int size);
}
