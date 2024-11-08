package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.member.domain.entity.Member;

import java.util.List;

public interface CouponService {

    // 쿠폰 전체 조회
    List<CouponDTO> findAllCoupons();

    // 쿠폰 등록
    CouponDTO registerCoupon(CouponRegisterRequestVO request, Admin admin, Member tutor);
}
