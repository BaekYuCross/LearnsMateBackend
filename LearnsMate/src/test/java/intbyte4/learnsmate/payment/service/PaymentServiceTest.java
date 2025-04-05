package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.common.test.TestDataFactory;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// https://sjh9708.tistory.com/195
@SpringBootTest
@Transactional // 각각의 테스트 메서드에 대해 트랜잭션을 시작하고, 테스트가 종료되면 롤백
public class PaymentServiceTest {

    @Autowired private TestDataFactory testDataFactory;
    @Autowired private PaymentService paymentService;
    @Autowired private PaymentRepository paymentRepository;

    @BeforeEach
    public void before(){
        paymentRepository.deleteAll();
        System.out.println("Test Before");
    }

    @AfterEach
    public void after(){
        System.out.println("Test After");
    }


    @Test
    void getAllPayments_결제내역이_정상조회되는지() {
        // given
        Payment payment = testDataFactory.createTestPaymentWithoutCoupon();

        // when
        List<PaymentDTO> result = paymentService.getAllPayments();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPaymentPrice()).isEqualTo(15000);
    }

    @Test
    void getPaymentDetails_정상조회된다() {
        // given
        Payment payment = testDataFactory.createTestPaymentWithoutCoupon();

        // when
        PaymentDTO result = paymentService.getPaymentDetails(payment.getPaymentCode());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPaymentCode()).isEqualTo(payment.getPaymentCode());
        assertThat(result.getPaymentPrice()).isEqualTo(15000);
    }

    @Test
    void getPaymentDetails_존재하지않는ID일경우_예외반환() {
        // given
        Long notExistId = -1L;

        // when & then
        CommonException ex = assertThrows(CommonException.class, () -> {
            paymentService.getPaymentDetails(notExistId);
        });
        assertThat(ex.getStatusEnum()).isEqualTo(StatusEnum.PAYMENT_NOT_FOUND);
    }

    @Test
    void getPaymentDetails_쿠폰이포함된결제_정상조회() {
        // given
        Payment savedPayment = testDataFactory.createTestPaymentWithCoupon();

        // when
        PaymentDTO result = paymentService.getPaymentDetails(savedPayment.getPaymentCode());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPaymentCode()).isEqualTo(savedPayment.getPaymentCode());
        assertThat(result.getPaymentPrice()).isEqualTo(15000);
        assertThat(result.getCouponIssuanceCode()).isEqualTo(
                savedPayment.getCouponIssuance().getCouponIssuanceCode()
        );
    }
}