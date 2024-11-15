package intbyte4.learnsmate.payment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture_by_student.domain.entity.QLectureByStudent;
import intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.entity.QPayment;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPaymentRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PaymentFilterDTO> findPaymentByFilters(PaymentFilterRequestVO request) {
        QPayment payment = QPayment.payment;
        QLectureByStudent lectureByStudent = QLectureByStudent.lectureByStudent;
        QLecture lecture = QLecture.lecture;
        QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;
        QLectureCategory lectureCategory = QLectureCategory.lectureCategory;

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqPaymentCode(request.getPaymentCode()))
                .and(betweenPaymentPrice(request.getMinPaymentPrice(), request.getMaxPaymentPrice()))
                .and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()))
                .and(eqLectureCode(request.getLectureCode()))
                .and(likeLectureTitle(request.getLectureTitle()))
                .and(betweenLecturePrice(request.getMinLecturePrice(), request.getMaxLecturePrice()))
                .and(eqTutorCode(request.getTutorCode()))
                .and(eqTutorName(request.getTutorName()))
                .and(eqStudentCode(request.getStudentCode()))
                .and(eqStudentName(request.getStudentName()))
                .and(eqLectureCategoryCode(request.getLectureCategoryCode()))
                .and(eqCouponIssuanceCode(request.getCouponIssuanceCode()))
                .and(likeCouponIssuanceName(request.getCouponIssuanceName()));

        return queryFactory
                .select(Projections.constructor(
                        PaymentFilterDTO.class,
                        payment.paymentCode,
                        payment.paymentPrice,
                        payment.createdAt,
                        lecture.lectureCode,
                        lecture.lectureTitle,
                        lecture.lecturePrice,
                        lecture.tutor.memberCode,
                        lecture.tutor.memberName,
                        lectureByStudent.student.memberCode,
                        lectureByStudent.student.memberName,
                        lectureCategory.lectureCategoryCode,
                        lectureCategory.lectureCategoryName,
                        payment.couponIssuance.couponIssuanceCode,
                        payment.couponIssuance.coupon.couponName
                ))
                .from(payment)
                .join(payment.lectureByStudent, lectureByStudent) // Payment → LectureByStudent
                .join(lectureByStudent.lecture, lecture)          // LectureByStudent → Lecture
                .join(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture)) // Lecture → LectureCategoryByLecture
                .join(lectureCategoryByLecture.lectureCategory, lectureCategory) // LectureCategoryByLecture → LectureCategory
                .where(builder)
                .fetch();
    }

    private BooleanExpression eqPaymentCode(Long paymentCode) {
        return paymentCode == null ? null : QPayment.payment.paymentCode.eq(paymentCode);
    }

    private BooleanExpression betweenPaymentPrice(Integer minPaymentPrice, Integer maxPaymentPrice) {
        if (minPaymentPrice == null && maxPaymentPrice == null) {
            return null;
        }

        if (minPaymentPrice == null) {
            return QPayment.payment.paymentCode.loe(maxPaymentPrice);
        }

        if (maxPaymentPrice == null) {
            return QPayment.payment.paymentCode.goe(minPaymentPrice);
        }

        return QPayment.payment.paymentCode.between(minPaymentPrice, maxPaymentPrice);
    }

    private BooleanExpression betweenCreatedAt(LocalDateTime startCreatedAt, LocalDateTime endCreatedAt) {
        if (startCreatedAt == null && endCreatedAt == null) {
            return null;
        }

        if (startCreatedAt == null) {
            return QPayment.payment.createdAt.loe(endCreatedAt);
        }

        if (endCreatedAt == null) {
            return QPayment.payment.createdAt.goe(startCreatedAt);
        }

        return QPayment.payment.createdAt.between(startCreatedAt, endCreatedAt);
    }

    private BooleanExpression eqLectureCode(String lectureCode) {
        return lectureCode == null ? null : QPayment.payment.lectureByStudent.lecture.lectureCode.eq(lectureCode);
    }

    private BooleanExpression likeLectureTitle(String lectureTitle) {
        return lectureTitle == null ? null : QPayment.payment.lectureByStudent.lecture.lectureTitle.likeIgnoreCase(lectureTitle);
    }

    private BooleanExpression betweenLecturePrice(Integer minLecturePrice, Integer maxLecturePrice) {
        if (minLecturePrice == null && maxLecturePrice == null) {
            return null;
        }

        if (minLecturePrice == null) {
            return QPayment.payment.lectureByStudent.lecture.lecturePrice.loe(maxLecturePrice);
        }

        if (maxLecturePrice == null) {
            return QPayment.payment.lectureByStudent.lecture.lecturePrice.goe(minLecturePrice);
        }

        return QPayment.payment.lectureByStudent.lecture.lecturePrice.between(minLecturePrice, maxLecturePrice);
    }

    private BooleanExpression eqTutorCode(Long tutorCode) {
        return tutorCode == null ? null : QPayment.payment.lectureByStudent.lecture.tutor.memberCode.eq(tutorCode);
    }

    private BooleanExpression eqTutorName(String tutorName) {
        return tutorName == null ? null : QPayment.payment.lectureByStudent.lecture.tutor.memberName.eq(tutorName);
    }

    private BooleanExpression eqStudentCode(Long studentCode) {
        return studentCode == null ? null : QPayment.payment.lectureByStudent.student.memberCode.eq(studentCode);
    }

    private BooleanExpression eqStudentName(String studentName) {
        return studentName == null ? null : QPayment.payment.lectureByStudent.student.memberName.eq(studentName);
    }

    private BooleanExpression eqLectureCategoryCode(Integer lectureCategoryCode) {
        return lectureCategoryCode == null ? null : QLectureCategoryByLecture.lectureCategoryByLecture
                .lectureCategory.lectureCategoryCode.eq(lectureCategoryCode);
    }

    private BooleanExpression eqCouponIssuanceCode(String couponIssuanceCode) {
        return couponIssuanceCode == null ? null : QPayment.payment.couponIssuance.couponIssuanceCode.eq(couponIssuanceCode);
    }

    private BooleanExpression likeCouponIssuanceName(String couponIssuanceName) {
        return couponIssuanceName == null ? null : QPayment.payment.couponIssuance.coupon.couponName.likeIgnoreCase(couponIssuanceName);
    }
}
