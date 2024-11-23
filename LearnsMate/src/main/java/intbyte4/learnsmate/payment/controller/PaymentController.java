package intbyte4.learnsmate.payment.controller;

import intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO;
import intbyte4.learnsmate.payment.service.PaymentFacade;
import intbyte4.learnsmate.lecture.service.LectureFacade;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.ResponseFindPaymentVO;
import intbyte4.learnsmate.payment.domain.vo.*;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.service.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final PaymentFacade paymentFacade;
    private final PaymentMapper paymentMapper;
    private final LectureMapper lectureMapper;
    private final LectureFacade lectureFacade;
    private final IssueCouponMapper issueCouponMapper;
    private final MemberMapper memberMapper;

    @Operation(summary = "결제 내역 및 월별 매출 데이터 조회 (전년도 데이터까지)")
    @GetMapping
    public ResponseEntity<PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>>> getPayments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> response = paymentFacade.getPaymentsWithGraph(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "특정 결제 내역 조회")
    @GetMapping("/{paymentCode}")
    public ResponseEntity<ResponseFindPaymentVO> getPaymentDetails(@PathVariable("paymentCode") Long paymentCode) {
        PaymentDetailDTO paymentDTO = paymentFacade.getPaymentDetails(paymentCode);
        return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.fromDtoToResponseVO(paymentDTO));
    }

    @Operation(summary = "결제 내역 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterPaymentVO> registerPayment(@RequestBody RequestRegisterPaymentVO requestRegisterPaymentVO) {
        IssueCouponDTO issueCouponDTO = issueCouponMapper.fromRequestRegisterIssueCouponPaymentVOToDTO(requestRegisterPaymentVO.getIssueCouponVO());
        MemberDTO memberDTO = memberMapper.fromRequestRegisterMemberPaymentVOToMemberDTO(requestRegisterPaymentVO.getMemberVO());
        LectureDTO lectureDTO = lectureMapper.fromRequestRegisterLecturePaymentVOToDTO(requestRegisterPaymentVO.getLectureVO());

        if (issueCouponDTO != null) {
            lectureDTO = lectureFacade.discountLecturePrice(lectureDTO, issueCouponDTO);
            PaymentDTO payment = paymentService.lectureAdaptedPayment(memberDTO, lectureDTO, issueCouponDTO);
            ResponseRegisterPaymentVO response = paymentMapper.fromPaymentDTOtoResponseRegisterPaymentVO(payment);
            return ResponseEntity.ok(response);
        } else {
            PaymentDTO payment = paymentService.lectureUnAdaptedPayment(memberDTO, lectureDTO);
            ResponseRegisterPaymentVO response = paymentMapper.fromPaymentDTOtoResponseRegisterPaymentVO(payment);
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "필터별 결제 내역 조회")
    @GetMapping("/filter")
    public ResponseEntity<List<PaymentFilterDTO>> getPaymentsByFilters(@RequestBody PaymentFilterRequestVO request) {
        List<PaymentFilterDTO> payments = paymentService.getPaymentsByFilters(request);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
//    // 직원이 예상 매출액과 할인 매출액을 비교해서 조회 (추가 예시 메서드)
//    @GetMapping("/revenue")
//    public ResponseEntity<String> getRevenueComparison() {
//        // 매출 비교 로직을 처리하고 응답을 반환하는 로직 작성
//        return ResponseEntity.ok("예상 매출액과 할인 매출액 비교 결과");
//    }

}