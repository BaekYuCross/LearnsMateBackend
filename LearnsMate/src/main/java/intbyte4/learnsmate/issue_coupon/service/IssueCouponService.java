package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.issue_coupon.domain.dto.AllIssuedCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface IssueCouponService {

    List<IssueCouponDTO> findAllIssuedCoupons();

    @Transactional
    IssueCouponDTO createAndSaveIssueCoupon(Member student, Long couponCode);

    List<IssueCouponDTO> findIssuedCouponsByStudent(Long studentCode);

    IssueCouponDTO useIssuedCoupon(Long studentCode, String couponIssuanceCode);

    @Transactional
    Map<String, List<IssueCouponDTO>> findAllStudentCoupons(Long studentCode);

    void updateCouponUseStatus(IssueCouponDTO issueCouponDTO, Member member, CouponEntity couponEntity);

    List<AllIssuedCouponDTO> getFilteredIssuedCoupons(IssueCouponFilterRequestVO request);


    // 보유중인 쿠폰 조회
//    @Transactional
//    List<IssueCouponDTO> findAllStudentCoupons(IssueCouponDTO dto, Long studentCode);

    // 사용한 쿠폰 조회
//    @Transactional
//    List<IssueCouponDTO> findAllUsedStudentCoupons(IssueCouponDTO dto, Long studentCode);
}
