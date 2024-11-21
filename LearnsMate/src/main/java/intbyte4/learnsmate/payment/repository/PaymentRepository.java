package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, CustomPaymentRepository {
    @Query("SELECT COUNT(p) FROM payment p WHERE p.lectureByStudent.lecture.lectureCode = :lectureCode")
    int countPaymentsByLectureCode(@Param("lectureCode") String lectureCode);
}
