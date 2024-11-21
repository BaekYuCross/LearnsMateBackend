package intbyte4.learnsmate.userpercampaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPerCampaignRepository extends JpaRepository<UserPerCampaign, Integer> {
    List<UserPerCampaign> findByCampaign(Campaign campaign);
}
