package intbyte4.learnsmate.campaign.repository;

import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignRepositoryCustom {
    Page<Campaign> searchBy(CampaignFilterDTO campaignFilterDTO, Pageable pageable);
    List<Campaign> searchByWithoutPaging(CampaignFilterDTO request);
}
