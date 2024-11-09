package intbyte4.learnsmate.userpercampaign.repository;

import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPerCampaignRepository extends JpaRepository<UserPerCampaign, Long> {
}
