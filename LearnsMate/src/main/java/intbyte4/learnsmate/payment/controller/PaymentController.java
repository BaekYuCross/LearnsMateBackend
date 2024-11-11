package intbyte4.learnsmate.payment.controller;

import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.vo.ResponseFindPaymentVO;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.service.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final PaymentMapper paymentMapper;


    @Operation(summary = "전체 결제 내역 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindPaymentVO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        List<ResponseFindPaymentVO> paymentVOs = payments.stream()
                .map(paymentMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(paymentVOs);
    }

    @Operation(summary = "특정 결제 내역 조회")
    @GetMapping("/{paymentCode}")
    public ResponseEntity<ResponseFindPaymentVO> getPaymentDetails(@PathVariable("paymentCode") Long paymentCode) {
        PaymentDTO paymentDTO = paymentService.getPaymentDetails(paymentCode);
        return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.fromDtoToResponseVO(paymentDTO));
    }

//    // 직원이 예상 매출액과 할인 매출액을 비교해서 조회 (추가 예시 메서드)
//    @GetMapping("/revenue")
//    public ResponseEntity<String> getRevenueComparison() {
//        // 매출 비교 로직을 처리하고 응답을 반환하는 로직 작성
//        return ResponseEntity.ok("예상 매출액과 할인 매출액 비교 결과");
//    }

}