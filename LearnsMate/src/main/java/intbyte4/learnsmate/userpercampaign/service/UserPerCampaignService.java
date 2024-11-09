package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;

public interface UserPerCampaignService {
    void registerUserPerCampaign(MemberDTO member, CampaignDTO campaign);

}
