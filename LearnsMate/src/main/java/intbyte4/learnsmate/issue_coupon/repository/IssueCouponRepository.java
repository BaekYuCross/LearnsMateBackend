package intbyte4.learnsmate.issue_coupon.repository;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("issueCouponRepository")
public interface IssueCouponRepository extends JpaRepository<IssueCoupon, Long> {

    @Query("SELECT ic FROM issueCoupon ic " +
            "WHERE ic.student.memberCode = :studentCode AND ic.coupon.couponFlag = true")
    List<IssueCoupon> findAllByStudentAndActiveCoupon(@Param("studentCode") Long studentCode);

}
