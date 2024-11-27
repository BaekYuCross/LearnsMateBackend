package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT p.lectureByStudent.lecture.lectureCode " +
            "FROM payment p " +
            "WHERE p.lectureByStudent.student.memberCode = :studentCode " +
            "ORDER BY p.createdAt DESC")
    List<String> findLectureCodesByStudent(@Param("studentCode") Long studentCode, Pageable pageable);

    @Query("SELECT p.lectureByStudent.lecture.lectureCode, COUNT(p) " +
            "FROM payment p " +
            "WHERE p.lectureByStudent.student.memberCode IN :studentCodes " +
            "AND p.lectureByStudent.lecture.lectureCode != :excludedLectureCode " +
            "AND p.createdAt > (SELECT MAX(pp.createdAt) " +
            "FROM payment pp " +
            "WHERE pp.lectureByStudent.student.memberCode = :studentCode " +
            "AND pp.lectureByStudent.lecture.lectureCode = :excludedLectureCode) " +
            "GROUP BY p.lectureByStudent.lecture.lectureCode " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> findRecommendedLectures(@Param("studentCodes") List<Long> studentCodes,
                                           @Param("excludedLectureCode") String excludedLectureCode,
                                           @Param("studentCode") Long studentCode,
                                           Pageable pageable);

    @Query("SELECT COUNT(DISTINCT p.lectureByStudent.student.memberCode) FROM payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Integer countDistinctStudentsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT p.lectureByStudent.student.memberCode) FROM payment p " +
            "WHERE p.lectureByStudent.lecture.lectureCode = :lectureCode " +
            "AND p.createdAt BETWEEN :startDate AND :endDate")
    Integer countDistinctStudentsByLectureCodeBetweenDates(
            @Param("lectureCode") String lectureCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM payment p WHERE p.paymentCode = :paymentCode")
    Optional<Payment> findByPaymentCode(@Param("paymentCode") Long paymentCode);
}
