package intbyte4.learnsmate.coupon.controller;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFilterResponseVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponRegisterResponseVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("couponController")
@RequestMapping("coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @Operation(summary = "쿠폰 전체 조회")
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponFindResponseVO>> getAllCoupons() {
        List<CouponDTO> couponDTOList = couponService.findAllCoupons();
        List<CouponFindResponseVO> responseList = couponMapper.fromDTOListToCouponFindVO(couponDTOList);

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Operation(summary = "쿠폰 단 건 조회")
    @GetMapping("/coupon/{couponCode}")
    public ResponseEntity<CouponFindResponseVO> getCouponByCouponCode(@PathVariable("couponCode") Long couponCode) {
        CouponDTO couponDTO = couponService.findCouponByCouponCode(couponCode);
        CouponFindResponseVO response = couponMapper.fromDTOToFindResponseVO(couponDTO);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "쿠폰 필터링 조회")
    @GetMapping("/filters")
    public ResponseEntity<List<CouponFilterResponseVO>> filterCoupons(@RequestBody CouponFilterRequestVO request) {
        List<CouponDTO> coupons = couponService.getCouponsByFilters(request);
        List<CouponFilterResponseVO> response = couponMapper.fromDTOToCouponFilterResponseVO(coupons);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "강사 - 쿠폰 등록")
    @PostMapping("/tutor/register")
    public ResponseEntity<CouponRegisterResponseVO> createCoupon (@RequestBody TutorCouponRegisterRequestVO request
            , Member tutor
            , CouponCategory couponCategory
            , Long lectureCode) {
        CouponDTO couponDTO = couponService.tutorRegisterCoupon(request, tutor, couponCategory, lectureCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponMapper.fromDTOToRegisterResponseVO(couponDTO));
    }
}
