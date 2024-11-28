package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CustomCouponRepository {

    // couponFlag가 true인 쿠폰 전체 조회
    List<CouponEntity> findAllByCouponFlagTrue();

    // couponFlag가 true인 쿠폰 전체 조회 + offset 페이징
    Page<CouponEntity> findAllByCouponFlagTrue(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true AND c.admin IS NOT NULL")
    Page<CouponEntity> findAllAdminCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true AND c.tutor IS NOT NULL")
    Page<CouponEntity> findAllTutorCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c JOIN couponByCampaign cbc ON c.couponCode = cbc.coupon.couponCode WHERE cbc.campaign.campaignCode = :campaignCode")
    Page<CouponEntity> findCouponsByCampaignCode(@Param("campaignCode") Long campaignCode, Pageable pageable);

}
