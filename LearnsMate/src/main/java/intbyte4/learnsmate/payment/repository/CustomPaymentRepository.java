package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentDetailDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPaymentRepository {

    Page<PaymentFilterDTO> findPaymentByFilters(PaymentFilterRequestVO request, Pageable pageable);

    // 필터링x 정렬o
    Page<Payment> findAllWithSort(Pageable pageable);

    Page<PaymentDetailDTO> findAllWithSort2(Pageable pageable);

    // 필터링o 정렬o
    Page<PaymentFilterDTO> findPaymentByFiltersWithSort(PaymentFilterRequestVO request, Pageable pageable);
}
