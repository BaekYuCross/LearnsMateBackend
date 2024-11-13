package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.util.List;

public interface CustomPaymentRepository {

    List<PaymentFilterDTO> findPaymentByFilters(PaymentFilterRequestVO request);
}
