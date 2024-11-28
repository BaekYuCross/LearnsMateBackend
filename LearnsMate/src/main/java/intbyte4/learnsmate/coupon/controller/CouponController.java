package intbyte4.learnsmate.coupon.controller;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.dto.RegisterCouponDTO;
import intbyte4.learnsmate.coupon.domain.vo.request.*;
import intbyte4.learnsmate.coupon.domain.vo.response.*;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon.service.CouponFacade;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCPageResponse;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.webmvc.core.service.RequestService;
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
    private final CouponFacade couponFacade;
    private final MemberService memberService;
    private final LectureMapper lectureMapper;
    private final AdminMapper adminMapper;
    private final AdminService adminService;

    @Operation(summary = "쿠폰 전체 조회")
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponFindResponseVO>> getAllCoupons() {
        List<CouponFindResponseVO> responseList = couponFacade.findAllCoupons();

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Operation(summary = "직원 등록 쿠폰 전체 조회")
    @GetMapping("/admin-coupons")
    public ResponseEntity<List<CouponFindResponseVO>> getAdminCoupons() {
        List<CouponFindResponseVO> responseList = couponFacade.findAdminRegisterCoupons();

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


    @Operation(summary = "쿠폰 단 건 조회")
    @GetMapping("/coupon/{couponCode}")
    public ResponseEntity<CouponFindResponseVO> getCouponByCouponCode(@PathVariable("couponCode") Long couponCode) {
        CouponFindResponseVO response = couponFacade.findCoupon(couponCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "쿠폰 필터링 조회")
    @PostMapping("/filters")
    public ResponseEntity<List<CouponFindResponseVO>> filterCoupons(@RequestBody CouponFilterRequestVO request) {
        log.info("쿠폰 필터링 요청 수신");
        try {
            CouponFilterDTO dto = couponMapper.fromFilterVOtoFilterDTO(request);
            log.info(dto.toString());
            List<CouponFindResponseVO> response = couponFacade.filterCoupon(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "직원 - 쿠폰 등록")
    @PostMapping("/admin/register")
    public ResponseEntity<CouponRegisterResponseVO> createCoupon(@RequestBody AdminCouponRegisterRequestVO request) {
        log.info("{}", request.toString());
        CouponDTO couponDTO = couponService.adminRegisterCoupon(couponMapper.adminRegisterRequestVOToDTO(request), request.getLectureCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(couponMapper.fromDTOToRegisterResponseVO(couponDTO));
    }

    @Operation(summary = "직원 - 쿠폰 수정")
    @PatchMapping("/admin/edit/{couponCode}")
    public ResponseEntity<?> editCoupon(@PathVariable("couponCode") Long couponCode, @RequestBody AdminCouponEditRequestVO request) {
        log.info("직원 쿠폰 수정 요청 : {}", request);
        try {
            CouponDTO couponDTO = couponMapper.fromEditRequestVOToDto(request);
            couponDTO.setCouponCode(couponCode);

            CouponDTO updatedCouponDTO = couponService.editAdminCoupon(couponDTO);

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

    @Operation(summary = "직원 - 쿠폰 삭제 (비활성화)")
    @PatchMapping("/admin/delete/{couponCode}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("couponCode") Long couponCode) {
        log.info("직원 쿠폰 삭제 요청: couponCode = {}", couponCode);
        try {
            CouponDTO updatedCoupon = couponService.deleteAdminCoupon(couponCode);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("직원 쿠폰 삭제 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }


    @Operation(summary = "강사 등록 쿠폰 전체 조회")
    @GetMapping("/tutor-coupons")
    public ResponseEntity<List<CouponFindResponseVO>> getTutorCoupons(){
        List<CouponFindResponseVO> responseList = couponFacade.findTutorRegisterCoupons();

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Operation(summary = "강사 - 쿠폰 등록")
    @PostMapping("/tutor/register")
    public ResponseEntity<CouponRegisterResponseVO> createCoupon(@RequestBody TutorCouponRegisterRequestVO request) {
        CouponDTO couponDTO = couponFacade.tutorRegisterCoupon
                (request, request.getTutorCode(), request.getCouponCategoryCode(), request.getLectureCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(couponMapper.fromDTOToRegisterResponseVO(couponDTO));
    }

    @Operation(summary = "강사 - 쿠폰 수정")
    @PatchMapping("/tutor/edit/{couponCode}")
    public ResponseEntity<?> tutorEditCoupon(@PathVariable("couponCode") Long couponCode, @RequestBody TutorCouponEditRequestVO request, Member tutor) {
        try {
            log.info("강사 쿠폰 수정 요청: {}", request);
            CouponDTO couponDTO = couponMapper.fromTutorEditRequestVOToDTO(request);
            couponDTO.setCouponCode(couponCode);
            couponDTO.setTutorCode(request.getTutorCode()); // 추가했습니다.

            CouponDTO updatedCouponDTO = couponService.editTutorCoupon(couponDTO);

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

    @Operation(summary = "강사 - 쿠폰 삭제")
    @PatchMapping("/tutor/delete/{couponCode}")
    public ResponseEntity<?> tutorDeleteCoupon(@PathVariable("couponCode") Long couponCode) {
        try {
            log.info("강사 쿠폰 삭제 요청: couponCode = {}", couponCode);
            CouponDTO updatedCoupon = couponService.tutorDeleteCoupon(couponCode);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("강사 쿠폰 삭제 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "강사 - 쿠폰 활성화")
    @PatchMapping("/tutor/activate/{couponCode}")
    public ResponseEntity<?> tutorActiveCoupon(@PathVariable("couponCode") Long couponCode) {
        try {
            log.info("강사 쿠폰 활성화 요청: {}", couponCode);
            CouponDTO updatedCoupon = couponService.tutorActivateCoupon(couponCode);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("쿠폰 활성화 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "강사 - 쿠폰 비활성화")
    @PatchMapping("/tutor/inactivate/{couponCode}")
    public ResponseEntity<?> tutorInactiveCoupon(@PathVariable("couponCode") Long couponCode) {
        try {
            log.info("강사 쿠폰 비활성화 요청: {}", couponCode);
            CouponDTO updatedCoupon = couponService.tutorInactiveCoupon(couponCode);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCoupon);
        } catch (CommonException e) {
            log.error("쿠폰 비활성화 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }
}
