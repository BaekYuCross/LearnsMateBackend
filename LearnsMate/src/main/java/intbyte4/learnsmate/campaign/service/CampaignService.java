package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;

public interface CampaignService {
    CampaignDTO registerCampaign(CampaignDTO request);

    CampaignDTO updateCampaign(CampaignDTO request, Long campaignCode);
}
