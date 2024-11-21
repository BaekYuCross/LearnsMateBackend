package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, CustomPaymentRepository {
    @Query("SELECT COUNT(p) FROM payment p WHERE p.lectureByStudent.lecture.lectureCode = :lectureCode")
    int countPaymentsByLectureCode(@Param("lectureCode") String lectureCode);

    @Query("SELECT new intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO(YEAR(p.createdAt), MONTH(p.createdAt), SUM(p.paymentPrice)) " +
            "FROM payment p " +
            "WHERE YEAR(p.createdAt) = :currentYear OR YEAR(p.createdAt) = :previousYear " +
            "GROUP BY YEAR(p.createdAt), MONTH(p.createdAt) " +
            "ORDER BY YEAR(p.createdAt), MONTH(p.createdAt)")
    List<PaymentMonthlyRevenueDTO> findMonthlyRevenueWithComparison(
            @Param("currentYear") int currentYear,
            @Param("previousYear") int previousYear);
}
