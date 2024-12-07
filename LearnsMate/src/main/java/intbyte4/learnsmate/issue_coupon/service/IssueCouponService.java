package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
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

    List<IssueCoupon> getFilteredIssuedCoupons(IssuedCouponFilterDTO dto);

    void issueCouponsToStudents(List<Long> studentCodeList, List<Long> couponCodeList);
}
