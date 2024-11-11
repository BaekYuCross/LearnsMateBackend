package intbyte4.learnsmate.campaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Primary
public interface CampaignRepository extends JpaRepository<Campaign, Long>, CampaignRepositoryCustom {
}
