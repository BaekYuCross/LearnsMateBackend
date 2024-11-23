package intbyte4.learnsmate.payment.repository;

import intbyte4.learnsmate.payment.domain.dto.PaymentMonthlyRevenueDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    // 선호 주제가 같은 학생들이 결제한 강의 중, 특정 강의 이후의 강의 데이터를 그룹화하여 추천
    // 특정 강의는 제외, 해당 강의 이후의 결제만 포함, 강의별로 그룹화, 결제 빈도에 따라 내림차순 정렬
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
}
