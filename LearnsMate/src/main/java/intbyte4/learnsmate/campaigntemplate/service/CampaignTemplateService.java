package intbyte4.learnsmate.campaigntemplate.service;

import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;

public interface CampaignTemplateService {
    CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO);

    CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO);

    CampaignTemplateDTO deleteTemplate(CampaignTemplateDTO campaignTemplateDTO);
}
