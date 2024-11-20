package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CustomCouponRepository {

    // couponFlag가 true인 쿠폰 전체 조회
    List<CouponEntity> findAllByCouponFlagTrue();

}
