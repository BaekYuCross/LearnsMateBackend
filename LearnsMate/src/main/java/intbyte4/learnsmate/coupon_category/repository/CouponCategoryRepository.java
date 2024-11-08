package intbyte4.learnsmate.coupon_category.repository;

import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Integer> {
}
