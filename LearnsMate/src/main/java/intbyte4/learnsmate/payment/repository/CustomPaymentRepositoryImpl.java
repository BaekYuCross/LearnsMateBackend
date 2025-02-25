package intbyte4.learnsmate.payment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture_by_student.domain.entity.QLectureByStudent;
import intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import intbyte4.learnsmate.member.domain.entity.QMember;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.entity.QPayment;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {
    private final JPAQueryFactory queryFactory;
    private final QPayment payment = QPayment.payment;
    private final QLectureByStudent lectureByStudent = QLectureByStudent.lectureByStudent;
    private final QLecture lecture = QLecture.lecture;
    private final QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;
    private final QLectureCategory lectureCategory = QLectureCategory.lectureCategory;

    @Override
    public Page<PaymentFilterDTO> findPaymentByFilters(PaymentFilterRequestVO request, Pageable pageable) {
        BooleanBuilder builder = createFilterConditions(request);

        if (request != null && request.getStudentCode() != null) {
            builder.and(lectureByStudent.student.memberCode.eq(request.getStudentCode()));
        }

        JPAQuery<Payment> baseQuery = queryFactory
                .selectFrom(payment)
                .innerJoin(payment.lectureByStudent, lectureByStudent)
                .innerJoin(lectureByStudent.student)
                .innerJoin(lectureByStudent.lecture, lecture)
                .leftJoin(lecture.tutor)
                .innerJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .innerJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .leftJoin(payment.couponIssuance)
                .leftJoin(payment.couponIssuance.coupon)
                .where(builder);

        Long total = baseQuery.clone()
                .select(payment.count())
                .fetch().get(0);

        List<PaymentFilterDTO> content = baseQuery
                .select(Projections.constructor(PaymentFilterDTO.class,
                        payment.paymentCode,
                        payment.createdAt,
                        lecture.lectureCode,
                        lecture.lectureTitle,
                        lectureCategory.lectureCategoryName.stringValue(),
                        lecture.tutor.memberCode,
                        lecture.tutor.memberName,
                        lectureByStudent.student.memberCode,
                        lectureByStudent.student.memberName,
                        payment.paymentPrice,
                        payment.couponIssuance.couponIssuanceCode,
                        payment.couponIssuance.coupon.couponName,
                        lecture.lecturePrice
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(payment.createdAt.desc())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder createFilterConditions(PaymentFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request != null) {
            builder.and(eqPaymentCode(request.getPaymentCode()))
                    .and(betweenPaymentPrice(request.getMinPaymentPrice(), request.getMaxPaymentPrice()))
                    .and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()))
                    .and(eqLectureCode(request.getLectureCode()))
                    .and(likeLectureTitle(request.getLectureTitle()))
                    .and(betweenLecturePrice(request.getMinLecturePrice(), request.getMaxLecturePrice()))
                    .and(eqTutorCode(request.getTutorCode()))
                    .and(eqTutorName(request.getTutorName()))
                    .and(eqStudentName(request.getStudentName()))
                    .and(eqLectureCategoryName(request.getLectureCategoryName()))
                    .and(eqCouponIssuanceCode(request.getCouponIssuanceCode()))
                    .and(likeCouponIssuanceName(request.getCouponIssuanceName()));
        }

        return builder;
    }

    private BooleanExpression eqPaymentCode(Long paymentCode) {
        return paymentCode != null ? payment.paymentCode.eq(paymentCode) : null;
    }

    private BooleanExpression betweenPaymentPrice(Integer minPaymentPrice, Integer maxPaymentPrice) {
        if (minPaymentPrice == null && maxPaymentPrice == null) return null;
        if (minPaymentPrice == null) return payment.paymentPrice.loe(maxPaymentPrice);
        if (maxPaymentPrice == null) return payment.paymentPrice.goe(minPaymentPrice);
        return payment.paymentPrice.between(minPaymentPrice, maxPaymentPrice);
    }

    private BooleanExpression betweenCreatedAt(LocalDateTime startCreatedAt, LocalDateTime endCreatedAt) {
        if (startCreatedAt == null && endCreatedAt == null) {
            return null; // 시작일과 종료일 모두 없으면 필터링 조건 없음
        }
        if (startCreatedAt == null) {
            return payment.createdAt.loe(endCreatedAt);
        }
        if (endCreatedAt == null) {
            return payment.createdAt.goe(startCreatedAt);
        }
        return payment.createdAt.between(startCreatedAt, endCreatedAt);
    }

    private BooleanExpression eqLectureCode(String lectureCode) {
        return StringUtils.hasText(lectureCode) ? lecture.lectureCode.likeIgnoreCase("%" + lectureCode + "%") : null;
    }

    private BooleanExpression likeLectureTitle(String lectureTitle) {
        return StringUtils.hasText(lectureTitle) ? lecture.lectureTitle.likeIgnoreCase("%" + lectureTitle + "%") : null;
    }

    private BooleanExpression betweenLecturePrice(Integer minLecturePrice, Integer maxLecturePrice) {
        if (minLecturePrice == null && maxLecturePrice == null) return null;
        if (minLecturePrice == null) return lecture.lecturePrice.loe(maxLecturePrice);
        if (maxLecturePrice == null) return lecture.lecturePrice.goe(minLecturePrice);
        return lecture.lecturePrice.between(minLecturePrice, maxLecturePrice);
    }

    private BooleanExpression eqTutorCode(Long tutorCode) {
        return tutorCode != null ? lecture.tutor.memberCode.eq(tutorCode) : null;
    }

    private BooleanExpression eqTutorName(String tutorName) {
        return StringUtils.hasText(tutorName) ? lecture.tutor.memberName.eq(tutorName) : null;
    }

    private BooleanExpression eqStudentName(String studentName) {
        return StringUtils.hasText(studentName) ? lectureByStudent.student.memberName.eq(studentName) : null;
    }

    private BooleanExpression eqLectureCategoryName(String categoryName) {
        return StringUtils.hasText(categoryName) ? lectureCategory.lectureCategoryName.stringValue().eq(categoryName) : null;
    }

    private BooleanExpression eqCouponIssuanceCode(String couponIssuanceCode) {
        return StringUtils.hasText(couponIssuanceCode) ? payment.couponIssuance.couponIssuanceCode.eq(couponIssuanceCode) : null;
    }

    private BooleanExpression likeCouponIssuanceName(String couponIssuanceName) {
        return StringUtils.hasText(couponIssuanceName) ? payment.couponIssuance.coupon.couponName.likeIgnoreCase("%" + couponIssuanceName + "%") : null;
    }

    @Override // 필터링x 정렬o
    public Page<Payment> findAllWithSort(Pageable pageable) {
        QPayment payment = QPayment.payment;
        QLectureByStudent lectureByStudent = QLectureByStudent.lectureByStudent;
        QLecture lecture = QLecture.lecture;
        QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;
        QLectureCategory lectureCategory = QLectureCategory.lectureCategory;
        QMember student = QMember.member;
        QMember tutor = new QMember("tutor");

        JPAQuery<Payment> query = queryFactory
                .selectFrom(payment)
                .innerJoin(payment.lectureByStudent, lectureByStudent)
                .innerJoin(lectureByStudent.student, student)
                .innerJoin(lectureByStudent.lecture, lecture)
                .innerJoin(lecture.tutor, tutor)
                .innerJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .innerJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .leftJoin(payment.couponIssuance);

        if (pageable.getSort().isSorted()) {
            OrderSpecifier<?> orderSpecifier = getOrderSpecifier(payment, lecture, lectureCategory, student, tutor, pageable.getSort().iterator().next());
            query.orderBy(orderSpecifier);
        }

        long total = query.fetchCount();

        List<Payment> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override // 필터링o 정렬o
    public Page<PaymentFilterDTO> findPaymentByFiltersWithSort(PaymentFilterRequestVO request, Pageable pageable) {
        QPayment payment = QPayment.payment;
        QLectureByStudent lectureByStudent = QLectureByStudent.lectureByStudent;
        QLecture lecture = QLecture.lecture;
        QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;
        QLectureCategory lectureCategory = QLectureCategory.lectureCategory;
        QMember student = QMember.member;
        QMember tutor = new QMember("tutor");

        BooleanBuilder builder = createFilterConditions(request);

        if (request != null && request.getStudentCode() != null) {
            builder.and(lectureByStudent.student.memberCode.eq(request.getStudentCode()));
        }

        JPAQuery<Payment> baseQuery = queryFactory
                .selectFrom(payment)
                .innerJoin(payment.lectureByStudent, lectureByStudent)
                .innerJoin(lectureByStudent.student, student)
                .innerJoin(lectureByStudent.lecture, lecture)
                .innerJoin(lecture.tutor, tutor)
                .innerJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .innerJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .leftJoin(payment.couponIssuance)
                .leftJoin(payment.couponIssuance.coupon)
                .where(builder);

        // 정렬 조건 적용
        if (pageable.getSort().isSorted()) {
            OrderSpecifier<?> orderSpecifier = getOrderSpecifier(payment, lecture, lectureCategory, student, tutor, pageable.getSort().iterator().next());
            baseQuery.orderBy(orderSpecifier);
        }

        Long total = baseQuery.clone()
                .select(payment.count())
                .fetch().get(0);

        List<PaymentFilterDTO> content = baseQuery
                .select(Projections.constructor(PaymentFilterDTO.class,
                        payment.paymentCode,
                        payment.createdAt,
                        lecture.lectureCode,
                        lecture.lectureTitle,
                        lectureCategory.lectureCategoryName.stringValue(),
                        lecture.tutor.memberCode,
                        lecture.tutor.memberName,
                        lectureByStudent.student.memberCode,
                        lectureByStudent.student.memberName,
                        payment.paymentPrice,
                        payment.couponIssuance.couponIssuanceCode,
                        payment.couponIssuance.coupon.couponName,
                        lecture.lecturePrice
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(QPayment payment, QLecture lecture, QLectureCategory lectureCategory, QMember student, QMember tutor, Sort.Order order) {
        String property = order.getProperty();
        boolean isAsc = order.getDirection().isAscending();

        return switch (property) {
            case "memberCode" -> isAsc ? student.memberCode.asc() : student.memberCode.desc();
            case "paymentCode" -> isAsc ? payment.paymentCode.asc() : payment.paymentCode.desc();
            case "paymentPrice" -> isAsc ? payment.paymentPrice.asc() : payment.paymentPrice.desc();
            case "createdAt" -> isAsc ? payment.createdAt.asc() : payment.createdAt.desc();
            case "lectureCode" -> isAsc ? lecture.lectureCode.asc() : lecture.lectureCode.desc();
            case "lectureTitle" -> isAsc ? lecture.lectureTitle.asc() : lecture.lectureTitle.desc();
            case "lecturePrice" -> isAsc ? lecture.lecturePrice.asc() : lecture.lecturePrice.desc();
            case "lectureCategory", "lectureCategoryName" -> isAsc ?
                    lectureCategory.lectureCategoryName.stringValue().asc() :
                    lectureCategory.lectureCategoryName.stringValue().desc();
            case "tutorCode" -> isAsc ? tutor.memberCode.asc() : tutor.memberCode.desc();
            case "tutorName" -> isAsc ? tutor.memberName.asc() : tutor.memberName.desc();
            case "studentCode" -> isAsc ? student.memberCode.asc() : student.memberCode.desc();
            case "studentName" -> isAsc ? student.memberName.asc() : student.memberName.desc();
            case "studentEmail" -> isAsc ? student.memberEmail.asc() : student.memberEmail.desc();
            case "couponIssuanceCode" -> isAsc ? payment.couponIssuance.couponIssuanceCode.asc() : payment.couponIssuance.couponIssuanceCode.desc();
            case "couponIssuanceName" -> isAsc ? payment.couponIssuance.coupon.couponName.asc() : payment.couponIssuance.coupon.couponName.desc();
            default -> payment.createdAt.desc();
        };
    }
}
