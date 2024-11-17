package intbyte4.learnsmate.issue_coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.issue_coupon.domain.QIssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.AllIssuedCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static intbyte4.learnsmate.coupon.domain.entity.QCouponEntity.couponEntity;
import static intbyte4.learnsmate.member.domain.entity.QMember.member;

@Repository
public class CustomIssueCouponRepositoryImpl implements CustomIssueCouponRepository {

    private final JPAQueryFactory queryFactory;

    public CustomIssueCouponRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<AllIssuedCouponDTO> findIssuedCouponsByFilters (IssueCouponFilterRequestVO request) {
        QIssueCoupon issueCoupon = QIssueCoupon.issueCoupon;

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqCouponIssuanceCode(request))
                .and(likeCouponName(request))
                .and(likeCouponContents(request))
                .and(eqCouponCategory(request))
                .and(eqStudentCode(request))
                .and(eqStudentName(request))
                .and(eqCouponUseStatus(request))
                .and(eqCouponDiscountRate(request))
                .and(betweenCouponStartDate(request))
                .and(betweenCouponEndDate(request))
                .and(betweenCouponUseDate(request))
                .and(betweenCouponIssueDate(request));

        return queryFactory
                .select(Projections.constructor(
                        AllIssuedCouponDTO.class,
                        issueCoupon.couponIssuanceCode,
                        couponEntity.couponName,
                        couponEntity.couponContents,
                        couponEntity.couponCategory,
                        member.memberCode,
                        member.memberName,
                        issueCoupon.couponUseStatus,
                        couponEntity.couponDiscountRate,
                        couponEntity.couponStartDate,
                        couponEntity.couponExpireDate,
                        issueCoupon.couponUseDate,
                        issueCoupon.couponIssueDate
                ))
                .from(issueCoupon)
                .join(issueCoupon.coupon, couponEntity)
                .join(issueCoupon.student, member)
                .where(builder)
                .fetch();
    }

    private BooleanExpression eqCouponIssuanceCode(IssueCouponFilterRequestVO request) {

        if (request.getCouponIssuanceCode() == null) {
            return null;
        }
        return QIssueCoupon.issueCoupon.couponIssuanceCode.eq(request.getCouponIssuanceCode());
    }

    private BooleanExpression likeCouponName(IssueCouponFilterRequestVO request) {

        if (request.getCouponName() == null) {
            return null;
        }
        return QIssueCoupon.issueCoupon.coupon.couponName.likeIgnoreCase("%" + request.getCouponName() + "%");
    }

    private BooleanExpression likeCouponContents(IssueCouponFilterRequestVO request) {

        if (request.getCouponContents() == null) {
            return null;
        }
        return QIssueCoupon.issueCoupon.coupon.couponContents.likeIgnoreCase("%" + request.getCouponContents() + "%");
    }

    private BooleanExpression eqCouponCategory(IssueCouponFilterRequestVO request) {
        Integer categoryCode = request.getCouponCategoryCode();

        if (request.getCouponCategory() == null) {
            return null;
        }

        return couponEntity.couponCategory.couponCategoryCode.eq(categoryCode);
    }

    private BooleanExpression eqStudentCode(IssueCouponFilterRequestVO request) {
        if (request.getStudentCode() == null) {
            return null;
        }

        return member.memberCode.eq(request.getStudentCode());
    }

    private BooleanExpression eqStudentName(IssueCouponFilterRequestVO request) {
        if (request.getStudentName() == null) {
            return null;
        }

        return member.memberName.eq(request.getStudentName());
    }

    private BooleanExpression eqCouponUseStatus(IssueCouponFilterRequestVO request) {
        if (request.getCouponUseStatus() == null) {
            return null;
        }

        return QIssueCoupon.issueCoupon.couponUseStatus.eq(request.getCouponUseStatus());
    }

    private BooleanExpression eqCouponDiscountRate(IssueCouponFilterRequestVO request) {
        if (request.getCouponDiscountRate() == null) {
            return null;
        }

        return couponEntity.couponDiscountRate.eq(request.getCouponDiscountRate());
    }

    private BooleanExpression betweenCouponStartDate(IssueCouponFilterRequestVO request) {

        if (request.getStartCouponStartDate() == null && request.getEndCouponStartDate() == null) {
            return null;
        }
        if (request.getStartCouponStartDate() == null) {
            return couponEntity.couponStartDate.loe(request.getEndCouponStartDate());
        }
        if (request.getEndCouponStartDate() == null) {
            return couponEntity.couponStartDate.goe(request.getStartCouponStartDate());
        }
        return couponEntity.couponStartDate.between(request.getStartCouponStartDate(), request.getEndCouponStartDate());
    }

    private BooleanExpression betweenCouponEndDate(IssueCouponFilterRequestVO request) {

        if (request.getStartCouponExpireDate() == null && request.getEndCouponExpireDate() == null) {
            return null;
        }

        if (request.getStartCouponExpireDate() == null) {
            return couponEntity.couponExpireDate.loe(request.getEndCouponExpireDate());
        }

        if (request.getEndCouponExpireDate() == null) {
            return couponEntity.couponExpireDate.goe(request.getStartCouponExpireDate());
        }

        return couponEntity.couponExpireDate.between(request.getStartCouponExpireDate(), request.getEndCouponExpireDate());
    }

    private BooleanExpression betweenCouponUseDate(IssueCouponFilterRequestVO request) {

        if (request.getStartCouponUseDate() == null && request.getEndCouponUseDate() == null) {
            return null;
        }

        if (request.getStartCouponUseDate() == null) {
            return QIssueCoupon.issueCoupon.couponUseDate.loe(request.getEndCouponUseDate());
        }

        if (request.getEndCouponUseDate() == null) {
            return QIssueCoupon.issueCoupon.couponUseDate.goe(request.getStartCouponUseDate());
        }

        return QIssueCoupon.issueCoupon.couponUseDate.between(request.getStartCouponUseDate(), request.getEndCouponUseDate());
    }

    private BooleanExpression betweenCouponIssueDate(IssueCouponFilterRequestVO request) {

        if (request.getStartCouponIssueDate() == null && request.getEndCouponIssueDate() == null) {
            return null;
        }

        if (request.getStartCouponIssueDate() == null) {
            return QIssueCoupon.issueCoupon.couponIssueDate.loe(request.getEndCouponIssueDate());
        }

        if (request.getEndCouponIssueDate() == null) {
            return QIssueCoupon.issueCoupon.couponIssueDate.goe(request.getStartCouponIssueDate());
        }

        return QIssueCoupon.issueCoupon.couponIssueDate.between(request.getStartCouponIssueDate(), request.getEndCouponIssueDate());
    }
}
