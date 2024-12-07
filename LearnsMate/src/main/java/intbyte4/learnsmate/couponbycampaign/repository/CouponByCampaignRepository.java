package intbyte4.learnsmate.couponbycampaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponByCampaignRepository extends JpaRepository<CouponByCampaign, Long> {
    List<CouponByCampaign> findByCampaign(Campaign getCampaignCode);
    void deleteByCampaign_CampaignCode(Long campaignCode);
}
