package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPaymentRepository {

    Page<PaymentFilterDTO> findPaymentByFilters(PaymentFilterRequestVO request, Pageable pageable);
}
