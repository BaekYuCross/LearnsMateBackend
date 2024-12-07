package intbyte4.learnsmate.campaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
@Primary
public interface CampaignRepository extends JpaRepository<Campaign, Long>, CampaignRepositoryCustom {

    // campaign_type이 RESERVATION 이고 lessthan이고 flag가 false인 것들에 한해서
    List<Campaign> findByCampaignSendDateLessThanEqualAndCampaignSendFlagFalseAndCampaignType(LocalDateTime currentTime, CampaignTypeEnum campaignTypeEnum);
}
