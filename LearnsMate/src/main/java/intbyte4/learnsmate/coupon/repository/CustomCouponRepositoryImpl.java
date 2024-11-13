package intbyte4.learnsmate.coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.entity.QCouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomCouponRepositoryImpl implements CustomCouponRepository {

    private final JPAQueryFactory queryFactory;

    public CustomCouponRepositoryImpl (EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CouponEntity> findCouponsByFilters(CouponFilterRequestVO request) {
        QCouponEntity coupon = QCouponEntity.couponEntity;

        // 동적 조건 조합
        BooleanBuilder builder = new BooleanBuilder()
                .and(likeCouponName(request.getCouponName()))
                .and(likeCouponContents(request.getCouponContents()))
                .and(eqCouponFlag(request.getCouponFlag()))
                .and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()))
                .and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()))
                .and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()))
                .and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()))
                .and(eqCouponCategoryCode(request.getCouponCategoryCode()))
                .and(eqAdminCode(request.getAdminCode()))
                .and(eqTutorCode(request.getTutorCode()));

        return queryFactory
                .selectFrom(coupon)
                .where(builder)
                .fetch();
    }

    // 조건별 메서드
    private BooleanExpression likeCouponName(String couponName) {
        return couponName == null ? null : QCouponEntity.couponEntity.couponName.likeIgnoreCase("%" + couponName + "%");
    }

    private BooleanExpression likeCouponContents(String couponContents) {
        return couponContents == null ? null : QCouponEntity.couponEntity.couponContents.likeIgnoreCase("%" + couponContents + "%");
    }

    private BooleanExpression eqCouponFlag(Boolean couponFlag) {
        return couponFlag == null ? null : QCouponEntity.couponEntity.couponFlag.eq(couponFlag);
    }

    private BooleanExpression betweenDiscountRate(Integer minDiscountRate, Integer maxDiscountRate) {
        if (minDiscountRate == null && maxDiscountRate == null) {
            return null;
        }

        if (minDiscountRate == null) {
            return QCouponEntity.couponEntity.couponDiscountRate.loe(maxDiscountRate);
        }

        if (maxDiscountRate == null) {
            return QCouponEntity.couponEntity.couponDiscountRate.goe(minDiscountRate);
        }

        return QCouponEntity.couponEntity.couponDiscountRate.between(minDiscountRate, maxDiscountRate);
    }

    private BooleanExpression betweenExpireDate(LocalDateTime startExpireDate, LocalDateTime endExpireDate) {
        if (startExpireDate == null && endExpireDate == null) {
            return null;
        }

        if (startExpireDate == null) {
            return QCouponEntity.couponEntity.couponExpireDate.loe(endExpireDate);
        }

        if (endExpireDate == null) {
            return QCouponEntity.couponEntity.couponExpireDate.goe(startExpireDate);
        }

        return QCouponEntity.couponEntity.couponExpireDate.between(startExpireDate, endExpireDate);
    }

    private BooleanExpression betweenCreatedAt(LocalDateTime startCreatedAt, LocalDateTime endCreatedAt) {
        if (startCreatedAt == null && endCreatedAt == null) {
            return null;
        }

        if (endCreatedAt == null) {
            return QCouponEntity.couponEntity.createdAt.loe(startCreatedAt);
        }

        if (startCreatedAt == null) {
            return QCouponEntity.couponEntity.createdAt.goe(endCreatedAt);
        }

        return QCouponEntity.couponEntity.createdAt.between(startCreatedAt, endCreatedAt);
    }

    private BooleanExpression betweenCouponStartDate(LocalDateTime startCouponStartDate, LocalDateTime endCouponStartDate) {
        if (startCouponStartDate == null && endCouponStartDate == null) {
            return null;
        }

        if (startCouponStartDate == null) {
            return QCouponEntity.couponEntity.couponStartDate.loe(endCouponStartDate);
        }

        if (endCouponStartDate == null) {
            return QCouponEntity.couponEntity.couponStartDate.goe(startCouponStartDate);
        }

        return QCouponEntity.couponEntity.couponStartDate.between(startCouponStartDate, endCouponStartDate);
    }

    private BooleanExpression eqCouponCategoryCode(Integer couponCategoryCode) {

        return couponCategoryCode == null ? null : QCouponEntity.couponEntity.couponCategory.couponCategoryCode.eq(couponCategoryCode);
    }

    private BooleanExpression eqAdminCode(Long adminCode) {
        return adminCode == null ? null : QCouponEntity.couponEntity.admin.adminCode.eq(adminCode);
    }

    private BooleanExpression eqTutorCode (Long tutorCode) {
        return tutorCode == null ? null : QCouponEntity.couponEntity.tutor.memberCode.eq(tutorCode);
    }
}

