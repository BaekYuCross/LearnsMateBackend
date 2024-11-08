package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.member.domain.entity.Member;

public interface CouponService {

    // 쿠폰 등록
    CouponDTO registerCoupon(CouponRegisterRequestVO request, Admin admin, Member tutor);
}
