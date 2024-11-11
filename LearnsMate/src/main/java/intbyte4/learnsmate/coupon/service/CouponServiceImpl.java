package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.repository.CouponRepository;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("couponService")
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    // 쿠폰 전체 조회
    @Override
    public List<CouponDTO> findAllCoupons() {

        List<CouponEntity> couponEntities = couponRepository.findAll();
        List<CouponDTO> couponDTOList = new ArrayList<>();
        couponEntities.forEach(dto -> couponDTOList.add(couponMapper.toDTO(dto)));

        return couponDTOList;
    }
    // 쿠폰 단 건 조회 (쿠폰코드로)
    @Override
    public CouponDTO findCouponByCouponCode(Long couponCode) {
        CouponEntity couponEntity = couponRepository.findById(couponCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        return couponMapper.toDTO(couponEntity);
    }

    @Override
    public CouponEntity findByCouponCode(Long couponCode) {
        CouponEntity couponEntity = couponRepository.findById(couponCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        return couponEntity;
    }

    // 쿠폰 필터링해서 조회
    @Override
    public List<CouponDTO> getCouponsByFilters(CouponFilterRequestVO request) {
        List<CouponEntity> entities = couponRepository.findCouponsByFilters(request);
        return entities.stream()
                .map(coupon -> couponMapper.toDTO(coupon))
                .collect(Collectors.toList());
    }

    // 쿠폰 등록
    @Override
    public CouponDTO registerCoupon(CouponRegisterRequestVO request, Admin admin, Member tutor, CouponCategory couponCategory) {

        /* todo. 따로 Mapper에 메소드 빼서 변환하기 */
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
                .couponCategory(couponCategory)
                .admin(admin)
                .tutor(tutor)
                .build();

        couponRepository.save(newCoupon);

        return couponMapper.toDTO(newCoupon);
    };
    // 쿠폰 수정
    // 쿠폰 삭제
}
