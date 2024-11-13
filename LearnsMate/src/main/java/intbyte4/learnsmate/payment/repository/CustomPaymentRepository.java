package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.util.List;

public interface CustomPaymentRepository {
    List<Payment> findPaymentByFilters(PaymentFilterRequestVO request);
}
