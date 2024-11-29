package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;

import java.util.List;

public interface UserPerCampaignService {
    void registerUserPerCampaign(MemberDTO member, CampaignDTO campaign);
    List<UserPerCampaignDTO> findByCampaignCode(Campaign campaign);
    void removeUserPerCampaign(Long userPerCampaignCode);
    void removeByCampaignCode(Long campaignCode);
    List<UserPerCampaignDTO> findUserByCampaignCode(Long campaignCode);
}
