package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.domain.CouponByLecture;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.AllIssuedCouponResponseVO;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueCouponFacade {

    private final MemberService memberService;
    private final IssueCouponService issueCouponService;
    private final MemberMapper memberMapper;
    private final CouponService couponService;
    private final CouponByLectureService couponByLectureService;
    private final LectureService lectureService;

    @Transactional
    public List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();

        for (Long studentCode : request.getStudentCodes()) {
            MemberDTO studentDTO = memberService.findByStudentCode(studentCode);
            Member student = memberMapper.fromMemberDTOtoMember(studentDTO);
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

    public List<AllIssuedCouponResponseVO> findAllIssuedCoupons() {
        List<IssueCouponDTO> issuedCouponDTOList = issueCouponService.findAllIssuedCoupons();
        List<AllIssuedCouponResponseVO> allIssuedCouponResponseVOList = new ArrayList<>();
        for (IssueCouponDTO issueCouponDTO : issuedCouponDTOList) {
            CouponEntity coupon = couponService.findByCouponCode(issueCouponDTO.getCouponCode());
            List<String> lectureCodes = couponByLectureService.getLectureCodesByCouponCode(coupon.getCouponCode());
            List<String> lectureNames = couponByLectureService.getLectureNamesByCouponCode(coupon.getCouponCode());
            List<String> tutorNames = couponByLectureService.getTutorNamesByCouponCode(coupon.getCouponCode());
            List<Integer> lecturePrices = couponByLectureService.getLecturePricesByCouponCode(coupon.getCouponCode());

            AllIssuedCouponResponseVO response = AllIssuedCouponResponseVO.builder()
                    .couponIssuanceCode(issueCouponDTO.getCouponIssuanceCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponCategoryName(coupon.getCouponCategory().getCouponCategoryName())
                    .studentCode(issueCouponDTO.getStudentCode())
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponUseStatus(issueCouponDTO.getCouponUseStatus())
                    .couponUseDate(issueCouponDTO.getCouponUseDate())
                    .couponIssueDate(issueCouponDTO.getCouponIssueDate())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .couponCode(issueCouponDTO.getCouponCode())
                    .lectureCode(lectureCodes)
                    .lectureName(lectureNames)
                    .tutorName(tutorNames)
                    .lecturePrice(lecturePrices)
                    .build();

            allIssuedCouponResponseVOList.add(response);
        }

        return allIssuedCouponResponseVOList;

    }
}

