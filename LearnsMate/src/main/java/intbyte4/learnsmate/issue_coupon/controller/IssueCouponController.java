package intbyte4.learnsmate.issue_coupon.controller;

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

    @Operation(summary = "학생에게 쿠폰 발급")
    @PostMapping("/register")
    public ResponseEntity<List<IssueCouponRegisterResponseVO>> registerIssuedCoupons(@RequestBody IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = issueCouponService.issueCouponsToStudents(request);
        List<IssueCouponRegisterResponseVO> responseList = issuedCoupons.stream()
                .map(issueCouponMapper::fromDtoToRegisterResponseVO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
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

}
