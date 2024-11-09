package intbyte4.learnsmate.campaign_template.repository;

import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignTemplateRepository extends JpaRepository<CampaignTemplate, Long> {
}
