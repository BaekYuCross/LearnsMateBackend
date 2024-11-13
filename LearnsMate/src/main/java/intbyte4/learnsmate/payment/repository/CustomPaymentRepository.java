package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.util.List;

public interface CustomPaymentRepository {

    List<PaymentDetailDTO> findPaymentByFilters(PaymentFilterRequestVO request);
}
