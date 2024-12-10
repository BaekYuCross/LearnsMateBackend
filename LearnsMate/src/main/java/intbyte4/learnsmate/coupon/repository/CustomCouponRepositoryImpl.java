package intbyte4.learnsmate.coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.admin.domain.entity.QAdmin;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.entity.QCouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import intbyte4.learnsmate.member.domain.entity.QMember;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
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
        builder.and(eqCouponFlag(request.getCouponFlag()));
        builder.and(eqActiveState(request.getActiveState()));
        builder.and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        builder.and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()));
        builder.and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()));
        builder.and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()));
        builder.and(eqCouponCategoryName(request.getCouponCategoryName()));
        builder.and(eqAdminName(request.getAdminName()));
        builder.and(eqTutorName(request.getTutorName()));

        // registrationType 필터 조건 추가
        BooleanBuilder registrationTypeFilter = new BooleanBuilder();
        if ("admin".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.admin.isNotNull());  // admin이 설정된 데이터
            registrationTypeFilter.and(coupon.tutor.isNull());    // tutor는 null이어야 함
        } else if ("tutor".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.tutor.isNotNull()); // tutor가 설정된 데이터
            registrationTypeFilter.and(coupon.admin.isNull());   // admin은 null이어야 함
        }
        builder.and(registrationTypeFilter);

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
        builder.and(eqCouponFlag(request.getCouponFlag()));
        builder.and(eqActiveState(request.getActiveState()));
        builder.and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        builder.and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()));
        builder.and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()));
        builder.and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()));
        builder.and(eqCouponCategoryName(request.getCouponCategoryName()));
        builder.and(eqAdminName(request.getAdminName()));
        builder.and(eqTutorName(request.getTutorName()));


        // registrationType 필터 조건 추가
        BooleanBuilder registrationTypeFilter = new BooleanBuilder();
        if ("admin".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.admin.isNotNull());  // admin이 설정된 데이터
            registrationTypeFilter.and(coupon.tutor.isNull());    // tutor는 null이어야 함
        } else if ("tutor".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.tutor.isNotNull()); // tutor가 설정된 데이터
            registrationTypeFilter.and(coupon.admin.isNull());   // admin은 null이어야 함
        }
        builder.and(registrationTypeFilter);

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
        return activeState == null ? null : QCouponEntity.couponEntity.activeState.eq(activeState);
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

    @Override
    public Page<CouponEntity> searchByWithSort(CouponFilterRequestVO request, Pageable pageable) {
        QCouponEntity coupon = QCouponEntity.couponEntity;
        QAdmin admin = QAdmin.admin;
        QMember tutor = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(likeCouponName(request.getCouponName()));
        builder.and(likeCouponContents(request.getCouponContents()));
        builder.and(eqCouponFlag(request.getCouponFlag()));
        builder.and(eqActiveState(request.getActiveState()));
        builder.and(betweenDiscountRate(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        builder.and(betweenExpireDate(request.getStartExpireDate(), request.getEndExpireDate()));
        builder.and(betweenCreatedAt(request.getStartCreatedAt(), request.getEndCreatedAt()));
        builder.and(betweenCouponStartDate(request.getStartCouponStartDate(), request.getEndCouponStartDate()));
        builder.and(eqCouponCategoryName(request.getCouponCategoryName()));
        builder.and(eqAdminName(request.getAdminName()));
        builder.and(eqTutorName(request.getTutorName()));

        // registrationType 필터 조건 추가
        BooleanBuilder registrationTypeFilter = new BooleanBuilder();
        if ("admin".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.admin.isNotNull());
            registrationTypeFilter.and(coupon.tutor.isNull());
        } else if ("tutor".equals(request.getRegistrationType())) {
            registrationTypeFilter.and(coupon.tutor.isNotNull());
            registrationTypeFilter.and(coupon.admin.isNull());
        }
        builder.and(registrationTypeFilter);

        // Join을 명시적으로 처리
        JPAQuery<CouponEntity> query = queryFactory
                .selectFrom(coupon)
                .leftJoin(coupon.admin, admin)
                .leftJoin(coupon.tutor, tutor)
                .where(builder);

        // 정렬 조건 적용
        if (pageable.getSort().isSorted()) {
            Sort.Order sortOrder = pageable.getSort().iterator().next();
            OrderSpecifier<?> orderSpecifier = getOrderSpecifier(coupon, sortOrder);
            query.orderBy(orderSpecifier);
        }

        long total = query.clone()
                .select(coupon.count())
                .fetchOne();

        List<CouponEntity> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(QCouponEntity coupon, Sort.Order order) {
        String property = order.getProperty();
        boolean isAsc = order.getDirection().isAscending();

        return switch (property) {
            case "couponCode" -> isAsc ? coupon.couponCode.asc() : coupon.couponCode.desc();
            case "couponName" -> isAsc ? coupon.couponName.asc() : coupon.couponName.desc();
            case "couponContents" -> isAsc ? coupon.couponContents.asc() : coupon.couponContents.desc();
            case "couponDiscountRate" -> isAsc ? coupon.couponDiscountRate.asc() : coupon.couponDiscountRate.desc();
            case "couponCategoryName" -> isAsc ? coupon.couponCategory.couponCategoryName.asc() : coupon.couponCategory.couponCategoryName.desc();
            case "couponFlag" -> isAsc ? coupon.couponFlag.asc() : coupon.couponFlag.desc();
            case "activeState" -> isAsc ? coupon.activeState.asc() : coupon.activeState.desc();
            case "couponStartDate" -> isAsc ? coupon.couponStartDate.asc() : coupon.couponStartDate.desc();
            case "couponExpireDate" -> isAsc ? coupon.couponExpireDate.asc() : coupon.couponExpireDate.desc();
            case "createdAt" -> isAsc ? coupon.createdAt.asc() : coupon.createdAt.desc();
            case "updatedAt" -> isAsc ? coupon.updatedAt.asc() : coupon.updatedAt.desc();
            case "adminName" -> isAsc ?
                    coupon.admin.adminName.asc().nullsLast() :
                    coupon.admin.adminName.desc().nullsLast();
            case "tutorName" -> isAsc ?
                    coupon.tutor.memberName.asc().nullsLast() :
                    coupon.tutor.memberName.desc().nullsLast();
            default -> coupon.createdAt.desc();
        };
    }
}
