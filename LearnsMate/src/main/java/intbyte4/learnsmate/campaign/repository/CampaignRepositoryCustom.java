package intbyte4.learnsmate.campaign.repository;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignRepositoryCustom {
    Page<Campaign> searchBy(CampaignFilterDTO campaignFilterDTO, Pageable pageable);
}
