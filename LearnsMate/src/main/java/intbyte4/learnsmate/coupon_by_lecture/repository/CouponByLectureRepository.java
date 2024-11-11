package intbyte4.learnsmate.coupon_by_lecture.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon_by_lecture.domain.CouponByLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("couponByLectureRepository")
public interface CouponByLectureRepository extends JpaRepository<CouponByLecture, Long> {

    CouponByLecture findByCoupon(CouponEntity coupon);

    @Query("SELECT cbl FROM couponByLecture cbl " +
            "JOIN cbl.lecture l " +
            "JOIN cbl.coupon c " +
            "WHERE l.tutorCode.memberCode = :tutorCode " +
            "AND c.couponFlag = true")
    List<CouponByLecture> findByTutorCode(@Param("tutorCode") Long tutorCode);
}
