package intbyte4.learnsmate.payment.controller;

import intbyte4.learnsmate.common.exception.CommonException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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
    public ResponseEntity<PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>>> getPayments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        PaymentPageResponse<ResponseFindPaymentVO, Map<Integer, List<PaymentMonthlyRevenueDTO>>> response = paymentFacade.getPaymentsWithGraph(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "특정 결제 내역 조회")
    @GetMapping("/{paymentCode}")
    public ResponseEntity<ResponseFindPaymentVO> getPaymentDetails(@PathVariable("paymentCode") Long paymentCode) {
        try {
            PaymentDetailDTO paymentDTO = paymentFacade.getPaymentDetails(paymentCode);
            return ResponseEntity.ok(paymentMapper.fromDtoToResponseVO(paymentDTO));
        } catch (CommonException e) {
            log.error("Payment not found with code: {}", paymentCode);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting payment details", e);
            return ResponseEntity.internalServerError().build();
        }
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
    @PostMapping("/filter")
    public ResponseEntity<Page<PaymentFilterDTO>> getPaymentsByFilters(@RequestBody PaymentFilterRequestVO request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Page<PaymentFilterDTO> payments = paymentService.getPaymentsByFilters(request, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
}