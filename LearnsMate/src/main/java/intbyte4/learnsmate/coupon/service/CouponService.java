package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;

import java.util.List;

public interface CouponService {

    // 쿠폰 전체 조회
    List<CouponDTO> findAllCoupons();

    // 쿠폰 단 건 조회 (쿠폰코드로)
    CouponDTO findCouponByCouponCode(Long couponCode);


    CouponEntity findByCouponCode(Long couponCode);

    // 쿠폰 필터링해서 조회
    List<CouponDTO> getCouponsByFilters(CouponFilterRequestVO request);

    // 쿠폰 등록
    CouponDTO tutorRegisterCoupon(TutorCouponRegisterRequestVO request, Member tutor, CouponCategory couponCategory, Long lectureCode);
}
