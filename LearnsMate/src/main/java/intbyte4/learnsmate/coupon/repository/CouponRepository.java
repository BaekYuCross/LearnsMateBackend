package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.dto.ClientFindCouponDTO;
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
    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true ORDER BY c.createdAt DESC")
    List<CouponEntity> findAllByCouponFlagTrue();

    // couponFlag가 true인 쿠폰 전체 조회 + offset 페이징
    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true ORDER BY c.createdAt DESC")
    Page<CouponEntity> findAllByCoupon(Pageable pageable);

    // 필터링x 정렬o
    @Query(value = "SELECT DISTINCT c FROM Coupon c " +
            "LEFT JOIN FETCH c.couponCategory " +
            "LEFT JOIN FETCH c.admin " +
            "LEFT JOIN FETCH c.tutor " +
            "WHERE c.couponFlag = true",
            countQuery = "SELECT COUNT(c) FROM Coupon c WHERE c.couponFlag = true")
    Page<CouponEntity> findAllByCouponWithSort(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true AND c.admin IS NOT NULL")
    Page<CouponEntity> findAllAdminCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.couponFlag = true AND c.tutor IS NOT NULL")
    Page<CouponEntity> findAllTutorCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c JOIN couponByCampaign cbc ON c.couponCode = cbc.coupon.couponCode WHERE cbc.campaign.campaignCode = :campaignCode")
    Page<CouponEntity> findCouponsByCampaignCode(@Param("campaignCode") Long campaignCode, Pageable pageable);

    @Query("SELECT new intbyte4.learnsmate.coupon.domain.dto.ClientFindCouponDTO(" +
            "l.lectureCode, l.lectureTitle, l.lecturePrice, " +
            "c.couponCode, c.couponDiscountRate, c.couponStartDate, c.couponExpireDate, c.activeState) " +
            "FROM Coupon c " +
            "JOIN couponByLecture cbl ON c.couponCode = cbl.coupon.couponCode " +
            "JOIN cbl.lecture l " +
            "WHERE c.couponFlag = true AND c.tutor.memberCode = :tutorCode")
    List<ClientFindCouponDTO> findAllClientCoupon(@Param("tutorCode") Long tutorCode);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Coupon c WHERE c.couponCode = :couponCode AND c.admin IS NOT NULL")
    boolean findByAdminCouponByCouponCode(@Param("couponCode") Long couponCode);
}
