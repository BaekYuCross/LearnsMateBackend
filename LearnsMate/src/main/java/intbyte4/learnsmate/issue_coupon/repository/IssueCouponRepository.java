package intbyte4.learnsmate.issue_coupon.repository;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("issueCouponRepository")
public interface IssueCouponRepository extends CrudRepository<IssueCoupon, Long> {
}
