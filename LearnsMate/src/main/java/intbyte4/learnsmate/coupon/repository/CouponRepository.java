package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CustomCouponRepository {
}
