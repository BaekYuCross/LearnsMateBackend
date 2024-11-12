package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCouponFacade {

    private final MemberService memberService;
    private final IssueCouponService issueCouponService;

    @Transactional
    public List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();

        for (Long studentCode : request.getStudentCodes()) {
            Member student = memberService.findByStudentCode(studentCode);
            issuedCoupons.addAll(issueCouponsToStudent(student, request.getCouponCodes()));
        }

        return issuedCoupons;
    }

    private List<IssueCouponDTO> issueCouponsToStudent(Member student, List<Long> couponCodes) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();

        for (Long couponCode : couponCodes) {
            IssueCouponDTO issueCoupon = issueCouponService.createAndSaveIssueCoupon(student, couponCode);
            issuedCoupons.add(issueCoupon);
        }
        return issuedCoupons;
    }
}

