package intbyte4.learnsmate.couponbycampaign.repository;

import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponByCampaignRepository extends JpaRepository<CouponByCampaign, Long> {
}
