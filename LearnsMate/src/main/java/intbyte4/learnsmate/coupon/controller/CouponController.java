package intbyte4.learnsmate.coupon.controller;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.vo.request.*;
import intbyte4.learnsmate.coupon.domain.vo.response.*;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.facade.CouponLectureFacade;
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
    private final CouponLectureFacade couponLectureFacade;

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
        CouponDTO couponDTO = couponService.findCouponDTOByCouponCode(couponCode);
        CouponFindResponseVO response = couponMapper.fromDTOToFindResponseVO(couponDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "쿠폰 필터링 조회")
    @GetMapping("/filters")
    public ResponseEntity<List<CouponFilterResponseVO>> filterCoupons(@RequestBody CouponFilterRequestVO request) {
        List<CouponDTO> coupons = couponService.getCouponsByFilters(request);
        List<CouponFilterResponseVO> response = couponMapper.fromDTOToCouponFilterResponseVO(coupons);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "직원 - 쿠폰 등록")
    @PostMapping("/admin/register")
    public ResponseEntity<CouponRegisterResponseVO> createCoupon(@RequestBody AdminCouponRegisterRequestVO request, Admin admin, CouponCategory couponCategory) {
        CouponDTO couponDTO = couponService.adminRegisterCoupon(request, admin, couponCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponMapper.fromDTOToRegisterResponseVO(couponDTO));
    }

    @Operation(summary = "강사 - 쿠폰 등록")
    @PostMapping("/tutor/register")
    public ResponseEntity<CouponRegisterResponseVO> createCoupon(@RequestBody TutorCouponRegisterRequestVO request, Member tutor, CouponCategory couponCategory, Long lectureCode) {
        CouponDTO couponDTO = couponLectureFacade.tutorRegisterCoupon(request, tutor, couponCategory, lectureCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponMapper.fromDTOToRegisterResponseVO(couponDTO));
    }

    @Operation(summary = "직원 - 쿠폰 수정")
    @PatchMapping("/admin/edit/{couponCode}")
    public ResponseEntity<?> editCoupon(@PathVariable("couponCode") Long couponCode, @RequestBody AdminCouponEditRequestVO request, Admin admin) {
        log.info("직원 쿠폰 수정 요청 : {}", request);
        try {
            CouponDTO couponDTO = couponMapper.fromEditRequestVOToDto(request);
            couponDTO.setCouponCode(couponCode);

            CouponDTO updatedCouponDTO = couponService.editAdminCoupon(couponDTO, admin);

            AdminCouponEditResponseVO response = couponMapper.fromDTOToEditResponseVO(updatedCouponDTO);

            log.info("직원 쿠폰 수정 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("직원 쿠폰 수정 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }
    @Operation(summary = "강사 - 쿠폰 수정")
    @PatchMapping("/tutor/edit/{couponCode}")
    public ResponseEntity<?> tutorEditCoupon(@PathVariable("couponCode") Long couponCode, @RequestBody TutorCouponEditRequestVO request, Member tutor) {
        try {
            log.info("강사 쿠폰 수정 요청: {}", request);
            CouponDTO couponDTO = couponMapper.fromTutorEditRequestVOToDTO(request);
            couponDTO.setCouponCode(couponCode);

            CouponDTO updatedCouponDTO = couponService.tutorEditCoupon(couponDTO, tutor);

            TutorCouponEditResponseVO response = couponMapper.fromTutorDTOToResponse(updatedCouponDTO);

            log.info("강사 쿠폰 수정 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("강사 쿠폰 수정 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "직원 - 쿠폰 삭제 (비활성화)")
    @PatchMapping("/admin/delete/{couponCode}")
    public ResponseEntity<?> deleteCoupon(@RequestParam("coupon_code") Long couponCode, Admin admin) {
        log.info("직원 쿠폰 삭제 요청: couponCode = {}", couponCode);
        try {
            CouponDTO updatedCoupon = couponService.deleteAdminCoupon(couponCode, admin);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("직원 쿠폰 삭제 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "강사 - 쿠폰 삭제")
    @PatchMapping("/tutor/delete/{couponCode}")
    public ResponseEntity<?> tutorDeleteCoupon(CouponDTO couponDTO, @RequestParam("coupon_code") Long couponCode, Member tutor) {
        try {
            log.info("강사 쿠폰 삭제 요청: couponCode = {}", couponCode);
            CouponDTO updatedCoupon = couponService.tutorDeleteCoupon(couponDTO, couponCode, tutor);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("강사 쿠폰 삭제 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "강사 - 쿠폰 비활성화")
    @PatchMapping("/tutor/inactivate/{couponCode}")
    public ResponseEntity<?> tutorInactiveCoupon(@RequestParam("coupon_code") Long couponCode, CouponDTO couponDTO, Member tutor) {
        try {
            log.info("강사 쿠폰 비활성화 요청: {}", couponDTO);
            CouponDTO updatedCoupon = couponService.tutorInactiveCoupon(couponCode, couponDTO, tutor);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("쿠폰 비활성화 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "강사 - 쿠폰 활성화")
    @PatchMapping("/tutor/activate/{couponCode}")
    public ResponseEntity<?> tutorActiveCoupon(@RequestParam("coupon_code") Long couponCode, CouponDTO couponDTO, Member tutor) {
        try {
            log.info("강사 쿠폰 활성화 요청: {}", couponDTO);
            CouponDTO updatedCoupon = couponService.tutorActivateCoupon(couponCode, couponDTO, tutor);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("쿠폰 활성화 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

}
