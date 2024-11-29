package intbyte4.learnsmate.coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.entity.QCouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomCouponRepositoryImpl implements CustomCouponRepository {

    private final JPAQueryFactory queryFactory;

    public CustomCouponRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CouponEntity> findCouponsByFilters(CouponFilterRequestVO request) {
        QCouponEntity coupon = QCouponEntity.couponEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(likeCouponName(request.getCouponName()));
        builder.and(likeCouponContents(request.getCouponContents()));
        builder.and(eqActiveState(request.getActiveState()));
        builder.and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        builder.and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()));
        builder.and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()));
        builder.and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()));
        builder.and(eqCouponCategoryName(request.getCouponCategoryName()));
        builder.and(eqAdminName(request.getAdminName()));
        builder.and(eqTutorName(request.getTutorName()));

        return queryFactory
                .selectFrom(coupon)
                .where(builder)
                .fetch();
    }

    // offset 필터링
    @Override
    public Page<CouponEntity> searchBy(CouponFilterRequestVO request, Pageable pageable) {
        QCouponEntity coupon = QCouponEntity.couponEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(likeCouponName(request.getCouponName()));
        builder.and(likeCouponContents(request.getCouponContents()));
        builder.and(eqActiveState(request.getActiveState()));
        builder.and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        builder.and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()));
        builder.and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()));
        builder.and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()));
        builder.and(eqCouponCategoryName(request.getCouponCategoryName()));
        builder.and(eqAdminName(request.getAdminName()));
        builder.and(eqTutorName(request.getTutorName()));

        List<CouponEntity> content = queryFactory
                .selectFrom(coupon)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression likeCouponName(String couponName) {
        return couponName == null || couponName.isBlank() ? null : QCouponEntity.couponEntity.couponName.likeIgnoreCase("%" + couponName + "%");
    }

    private BooleanExpression likeCouponContents(String couponContents) {
        return couponContents == null || couponContents.isBlank() ? null : QCouponEntity.couponEntity.couponContents.likeIgnoreCase("%" + couponContents + "%");
    }

    private BooleanExpression eqActiveState(Boolean activeState) {
        return activeState == null ? null : QCouponEntity.couponEntity.couponFlag.eq(activeState);
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

        if (startCreatedAt == null) {
            return QCouponEntity.couponEntity.createdAt.loe(endCreatedAt);
        }

        if (endCreatedAt == null) {
            return QCouponEntity.couponEntity.createdAt.goe(startCreatedAt);
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

    private BooleanExpression eqCouponCategoryName(String couponCategoryName) {
        return couponCategoryName == null || couponCategoryName.isBlank() ? null :
                QCouponEntity.couponEntity.couponCategory.couponCategoryName.equalsIgnoreCase(couponCategoryName);
    }

    private BooleanExpression eqAdminName(String adminName) {
        return adminName == null || adminName.isBlank() ? null :
                QCouponEntity.couponEntity.admin.adminName.equalsIgnoreCase(adminName);
    }

    private BooleanExpression eqTutorName(String tutorName) {
        return tutorName == null || tutorName.isBlank() ? null :
                QCouponEntity.couponEntity.tutor.memberName.equalsIgnoreCase(tutorName);
    }
}
