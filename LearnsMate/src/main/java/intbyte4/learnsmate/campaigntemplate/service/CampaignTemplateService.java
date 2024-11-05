package intbyte4.learnsmate.campaigntemplate.service;

import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;

import java.util.List;

public interface CampaignTemplateService {
    CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO);

    CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO);

    void deleteTemplate(CampaignTemplateDTO campaignTemplateDTO);

    List<CampaignTemplateDTO> findAllByTemplate();

    CampaignTemplateDTO findByTemplateCode(CampaignTemplateDTO campaignTemplateDTO);
}
