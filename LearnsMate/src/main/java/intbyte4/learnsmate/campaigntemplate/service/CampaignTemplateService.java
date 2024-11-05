package intbyte4.learnsmate.campaigntemplate.service;

import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;
import org.springframework.stereotype.Service;

public interface CampaignTemplateService {
    CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO);
}
