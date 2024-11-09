package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("issueCouponService")
@RequiredArgsConstructor
public class IssueCouponServiceImpl implements IssueCouponService {

    private final IssueCouponRepository issueCouponRepository;
    private final IssueCouponMapper issueCouponMapper;
    private final MemberService memberService;
    private final CouponService couponService;

    @Override
    public List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();
        registerIssueCoupon(request, issuedCoupons);
        return issuedCoupons;
    }

    @Override
    public List<IssueCouponDTO> findIssuedCouponsByStudent(Long studentCode) {
        List<IssueCoupon> issueCouponList = issueCouponRepository.findAllByStudentAndActiveCoupon(studentCode);
        return issueCouponList.stream()
                .map(issueCouponMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void registerIssueCoupon(IssueCouponRegisterRequestVO request, List<IssueCouponDTO> issuedCoupons) {
        for (Long studentCode : request.getStudentCodes()) {
            Member student = memberService.findByStudentCode(studentCode);
            for (Long couponCode : request.getCouponCodes()) {
                CouponEntity coupon = couponService.findByCouponCode(couponCode);

                IssueCoupon issueCoupon = setCouponIssuanceCode(coupon, student);

                issueCouponRepository.save(issueCoupon);
                issuedCoupons.add(issueCouponMapper.toDTO(issueCoupon));
            }
        }
    }

    private IssueCoupon setCouponIssuanceCode(CouponEntity coupon, Member student) {
        int couponCategoryCode = coupon.getCouponCategory().getCouponCategoryCode();
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueCode = UUID.randomUUID().toString().substring(0, 8);
        String couponIssuanceCode = String.format("C%s-%s%s", couponCategoryCode, formattedDate, uniqueCode);

        return getIssueCoupon(coupon, student, couponIssuanceCode);
    }

    private static IssueCoupon getIssueCoupon(CouponEntity coupon, Member student, String couponIssuanceCode) {
        return IssueCoupon.builder()
                .couponIssuanceCode(couponIssuanceCode)
                .couponIssueDate(LocalDateTime.now())
                .couponUseStatus(false)
                .couponUseDate(null)
                .student(student)
                .coupon(coupon)
                .build();
    }

}
