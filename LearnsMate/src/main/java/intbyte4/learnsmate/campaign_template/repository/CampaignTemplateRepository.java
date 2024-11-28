package intbyte4.learnsmate.campaign_template.repository;

import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignTemplateRepository extends JpaRepository<CampaignTemplate, Long>, CampaignTemplateRepositoryCustom {
    List<CampaignTemplate> findAllByCampaignTemplateFlag(Sort createdAt, Boolean campaignTemplateFlag);
}
