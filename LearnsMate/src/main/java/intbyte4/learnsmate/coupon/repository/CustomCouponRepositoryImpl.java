package intbyte4.learnsmate.coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.entity.QCouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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
        BooleanBuilder builder = new BooleanBuilder();

        if (request.getCouponName() != null) {
            builder.and(coupon.couponName.likeIgnoreCase("%" + request.getCouponName() + "%"));
        }
        if (request.getCouponContents() != null) {
            builder.and(coupon.couponContents.likeIgnoreCase("%" + request.getCouponContents() + "%"));
        }
        if (request.getCouponFlag() != null) {
            builder.and(coupon.couponFlag.eq(request.getCouponFlag()));
        }
        if (request.getMinDiscountRate() != null && request.getMaxDiscountRate() != null) {
            builder.and(coupon.couponDiscountRate.between(request.getMinDiscountRate(), request.getMaxDiscountRate()));
        }
        if (request.getStartExpireDate() != null && request.getEndExpireDate() != null) {
            builder.and(coupon.couponExpireDate.between(request.getStartExpireDate(), request.getEndExpireDate()));
        }
        if (request.getStartCreatedAt() != null) {
            builder.and(coupon.createdAt.goe(request.getStartCreatedAt()));
        }
        if (request.getEndCreatedAt() != null) {
            builder.and(coupon.createdAt.loe(request.getEndCreatedAt()));
        }
        if (request.getStartCouponStartDate() != null) {
            builder.and(coupon.couponStartDate.goe(request.getStartCouponStartDate()));
        }
        if (request.getEndCouponStartDate() != null) {
            builder.and(coupon.couponStartDate.loe(request.getEndCouponStartDate()));
        }
        if (request.getCouponCategoryCode() != null) {
            builder.and(coupon.couponCategory.couponCategoryCode.eq(request.getCouponCategoryCode()));
        }
        if (request.getAdminCode() != null) {
            builder.and(coupon.admin.adminCode.eq(request.getAdminCode()));
        }
        if (request.getTutorCode() != null) {
            builder.and(coupon.tutor.memberCode.eq(request.getTutorCode()));
        }

        return queryFactory
                .selectFrom(coupon)
                .where(builder)
                .fetch();
    }
}
