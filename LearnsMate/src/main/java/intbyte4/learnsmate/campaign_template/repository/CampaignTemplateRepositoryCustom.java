package intbyte4.learnsmate.campaign_template.repository;

import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignTemplateRepositoryCustom {
    Page<CampaignTemplate> searchBy(CampaignTemplateFilterDTO campaignTemplateFilterDTO, Pageable pageable);
    List<CampaignTemplate> searchByWithoutPaging(CampaignTemplateFilterDTO request);
}
