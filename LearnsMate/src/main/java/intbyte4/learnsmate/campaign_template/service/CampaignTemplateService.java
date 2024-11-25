package intbyte4.learnsmate.campaign_template.service;

import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaign_template.domain.dto.FindAllCampaignTemplatesDTO;

import java.util.List;

public interface CampaignTemplateService {
    CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO);

    CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO);

    void deleteTemplate(CampaignTemplateDTO campaignTemplateDTO);

    List<FindAllCampaignTemplatesDTO> findAllByTemplate();

    CampaignTemplateDTO findByTemplateCode(Long campaignTemplateCode);
}
