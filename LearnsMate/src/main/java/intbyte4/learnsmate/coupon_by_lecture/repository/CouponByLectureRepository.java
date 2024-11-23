package intbyte4.learnsmate.coupon_by_lecture.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon_by_lecture.domain.CouponByLecture;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
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
            "WHERE l.tutor.memberCode = :tutorCode " +
            "AND c.couponFlag = true")
    List<CouponByLecture> findByTutorCode(@Param("tutorCode") Long tutorCode);

    CouponByLecture findByLectureAndCoupon(Lecture lecture, CouponEntity coupon);

    @Query("SELECT cbl.lecture.lectureCode FROM couponByLecture cbl WHERE cbl.coupon.couponCode = :couponCode")
    List<String> findLectureCodesByCouponCode(Long couponCode);

    @Query("SELECT l.lectureTitle FROM couponByLecture cbl JOIN cbl.lecture l WHERE cbl.coupon.couponCode = :couponCode")
    List<String> findLectureNamesByCouponCode(@Param("couponCode") Long couponCode);

    @Query("SELECT l.tutor.memberName FROM couponByLecture cbl JOIN cbl.lecture l WHERE cbl.coupon.couponCode = :couponCode")
    List<String> findTutorNamesByCouponCode(@Param("couponCode") Long couponCode);

    @Query("SELECT l.lecturePrice FROM couponByLecture cbl JOIN cbl.lecture l WHERE cbl.coupon.couponCode = :couponCode")
    List<Integer> findLecturePricesByCouponCode(@Param("couponCode") Long couponCode);


}
