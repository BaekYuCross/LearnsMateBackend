package intbyte4.learnsmate.issue_coupon.controller;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
import intbyte4.learnsmate.issue_coupon.domain.pagination.IssueCouponPageResponse;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.AllIssuedCouponResponseVO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponFacade;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponFindResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponRegisterResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponUseResponseVO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("issueCouponController")
@RequestMapping("issue-coupon")
@Slf4j
@RequiredArgsConstructor
public class IssueCouponController {

    private final IssueCouponService issueCouponService;
    private final IssueCouponMapper issueCouponMapper;
    private final IssueCouponFacade issueCouponFacade;

    @Operation(summary = "학생에게 쿠폰 발급")
    @PostMapping("/register")
    public ResponseEntity<List<IssueCouponRegisterResponseVO>> registerIssuedCoupons(@RequestBody IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = issueCouponFacade.issueCouponsToStudents(request);
        List<IssueCouponRegisterResponseVO> responseList = issuedCoupons.stream()
                .map(issueCouponMapper::fromDtoToRegisterResponseVO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    @Operation(summary = "학생이 자신의 발급된 쿠폰 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<List<IssueCouponFindResponseVO>> findIssuedCoupons(@RequestParam("student_code") Long studentCode) {
        List<IssueCouponDTO> issuedCoupons = issueCouponService.findIssuedCouponsByStudent(studentCode);
        List<IssueCouponFindResponseVO> responseList = issuedCoupons.stream()
                .map(issueCouponMapper::fromDtoToFindResponseVO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Operation(summary = "학생이 발급된 쿠폰 사용")
    @PatchMapping("/use")
    public ResponseEntity<IssueCouponUseResponseVO> useIssuedCoupon(
            @RequestParam("student_code") Long studentCode,
            @RequestParam("coupon_issuance_code") String couponIssuanceCode) {
        IssueCouponDTO usedCoupon = issueCouponService.useIssuedCoupon(studentCode, couponIssuanceCode);
        IssueCouponUseResponseVO response = issueCouponMapper.fromDtoToUseResponseVO(usedCoupon);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "발급된 쿠폰 전체 조회")
    @GetMapping("/all-issued-coupons")
    public ResponseEntity<List<AllIssuedCouponResponseVO>> getAllIssuedCoupons() {
        List<AllIssuedCouponResponseVO> allIssuedCoupons = issueCouponFacade.findAllIssuedCoupons();
        return new ResponseEntity<>(allIssuedCoupons, HttpStatus.OK);
    }

    @Operation(summary = "발급된 쿠폰 필터링 조회")
    @PostMapping("/filters")
    public ResponseEntity<List<AllIssuedCouponResponseVO>> findIssuedCouponsByFilters(@RequestBody IssueCouponFilterRequestVO request) {
        log.info("발급 쿠폰 필터링 요청 수신");
        try {
            IssuedCouponFilterDTO dto = issueCouponMapper.fromVOToFilterResponseDTO(request);
            log.info(dto.toString());
            List<AllIssuedCouponResponseVO> response = issueCouponFacade.filterIssuedCoupon(dto);
            log.info(response.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "발급된 쿠폰 전체 조회 - offset pagination")
    @GetMapping("/all-issued-coupons2")
    public ResponseEntity<IssueCouponPageResponse<AllIssuedCouponResponseVO>> getAllIssuedCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        IssueCouponPageResponse<AllIssuedCouponResponseVO> response;

        response = issueCouponFacade.findAllIssuedCoupons(page, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "발급된 쿠폰 필터링 조회 - offset pagination")
    @PostMapping("/filters2")
    public ResponseEntity<IssueCouponPageResponse<AllIssuedCouponResponseVO>> findIssuedCouponsByFilters(
            @RequestBody IssueCouponFilterRequestVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        log.info("발급 쿠폰 필터링 요청 수신");
        try {
            IssuedCouponFilterDTO dto = issueCouponMapper.fromVOToFilterResponseDTO(request);
            log.info(dto.toString());

            IssueCouponPageResponse<AllIssuedCouponResponseVO> response
                    = issueCouponFacade.filterIssuedCoupon(page, size, dto);

            log.info(response.toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
