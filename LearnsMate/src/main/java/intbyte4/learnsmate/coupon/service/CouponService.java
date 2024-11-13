package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.AdminCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CouponService {

    List<CouponDTO> findAllCoupons();

    CouponDTO findCouponDTOByCouponCode(Long couponCode);

    CouponEntity findByCouponCode(Long couponCode);

    List<CouponDTO> getCouponsByFilters(CouponFilterRequestVO request);

    CouponDTO adminRegisterCoupon(AdminCouponRegisterRequestVO request, Admin admin, CouponCategory couponCategory);

    CouponDTO editAdminCoupon(CouponDTO couponDTO, Admin admin);

    CouponDTO tutorEditCoupon(CouponDTO couponDTO, Member tutor);

    CouponDTO deleteAdminCoupon(Long couponCode, Admin admin);

    CouponDTO tutorDeleteCoupon(CouponDTO couponDTO, Long couponCode, Member tutor);

    @Transactional
    CouponDTO tutorInactiveCoupon(Long couponCode, CouponDTO couponDTO, Member tutor);

    @Transactional
    CouponDTO tutorActivateCoupon(Long couponCode, CouponDTO couponDTO, Member tutor);

    @Transactional
    void saveCoupon(CouponEntity couponEntity);
}
