package intbyte4.learnsmate.issue_coupon.controller;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponRegisterResponseVO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .map(issueCouponMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }
}
