package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
//
//    // 직원이 전체 결제 내역을 조회
//    public List<PaymentDTO> getAllPayments() {
//        List<Payment> payments = paymentRepository.findAll();
//        return payments.stream()
//                .map(paymentMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//
//    // 직원이 특정 결제 내역을 단건 상세 조회
//    public PaymentDTO getPaymentDetails(Long paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));
//        return new paymentMapper.toDTO(payment);
//    }

    // 직원이 예상 매출액과 할인 매출액을 비교해서 조회

    // 직원이 기간 별 매출액을 리스트와 그래프로 조회

}
