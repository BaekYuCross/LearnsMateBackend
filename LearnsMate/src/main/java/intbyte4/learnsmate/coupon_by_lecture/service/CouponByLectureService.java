package intbyte4.learnsmate.coupon_by_lecture.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;

import java.util.List;

public interface CouponByLectureService {
    void registerCouponByLecture(Lecture lecture, CouponEntity couponEntity);

    void updateCouponByLecture(CouponEntity coupon);

    List<CouponByLectureDTO> findCouponByLecture(Long tutorCode);

    CouponByLectureDTO findByCouponAndLecture(Lecture lecture, CouponEntity coupon);

    List<String> getLectureCodesByCouponCode(Long couponCode);

    List<String> getLectureNamesByCouponCode(Long couponCode);

    List<String> getTutorNamesByCouponCode(Long couponCode);

    List<Integer> getLecturePricesByCouponCode(Long couponCode);
}
