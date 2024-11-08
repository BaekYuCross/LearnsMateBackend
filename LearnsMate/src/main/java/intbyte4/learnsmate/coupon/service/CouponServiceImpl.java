package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.repository.CouponRepository;
import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    // 쿠폰 전체 조회
    // 쿠폰 단 건 조회 (쿠폰코드로)
    // 쿠폰 필터링해서 조회

    // 쿠폰 등록
    @Override
    public CouponDTO registerCoupon(CouponRegisterRequestVO request, Admin admin, Member tutor) {

        CouponEntity newCoupon = CouponEntity.builder()
                .couponCode(request.getCouponCode())
                .couponName(request.getCouponName())
                .couponContents(request.getCouponContents())
                .couponDiscountRate(request.getCouponDiscountRate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .couponStartDate(request.getCouponStartDate())
                .couponExpireDate(request.getCouponExpireDate())
                .couponFlag(true)
                .couponCategoryCode(request.getCouponCategoryCode())
                .admin(admin)
                .tutor(tutor)
                .build();

        couponRepository.save(newCoupon);

        return couponMapper.toDTO(newCoupon);
    };
    // 쿠폰 수정
    // 쿠폰 삭제
}
