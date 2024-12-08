package intbyte4.learnsmate.issue_coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.QIssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import jakarta.persistence.EntityManager;
import intbyte4.learnsmate.coupon.domain.entity.QCouponEntity;
import intbyte4.learnsmate.member.domain.entity.QMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static intbyte4.learnsmate.coupon.domain.entity.QCouponEntity.couponEntity;
import static intbyte4.learnsmate.issue_coupon.domain.QIssueCoupon.issueCoupon;
import static intbyte4.learnsmate.member.domain.entity.QMember.member;

@Slf4j
@Repository
public class CustomIssueCouponRepositoryImpl implements CustomIssueCouponRepository {

    private final JPAQueryFactory queryFactory;

    public CustomIssueCouponRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<IssueCoupon> findIssuedCouponsByFilters(IssueCouponFilterRequestVO request) {
        QIssueCoupon issueCoupon = QIssueCoupon.issueCoupon;
        QMember member = QMember.member;
        QCouponEntity couponEntity = QCouponEntity.couponEntity;

        BooleanBuilder builder = new BooleanBuilder()
                .and(likeCouponName(request))
                .and(likeCouponContents(request))
                .and(eqCouponCategoryName(request))
                .and(eqStudentCode(request))
                .and(likeStudentName(request))
                .and(eqCouponUseStatus(request))
                .and(betweenDiscountRate(request))
                .and(betweenCouponStartDate(request))
                .and(betweenCouponEndDate(request))
                .and(betweenCouponUseDate(request))
                .and(betweenCouponIssueDate(request));

        // 필터링된 IssueCoupon 엔티티를 반환
        return queryFactory
                .selectFrom(issueCoupon)
                .join(issueCoupon.coupon, couponEntity).fetchJoin()
                .join(issueCoupon.student, member).fetchJoin()
                .where(builder)
                .select(issueCoupon)
                .fetch();
    }

    // 이슈 쿠폰 필터링 offset 페이지네이션
    @Override
    public Page<IssueCoupon> getFilteredIssuedCoupons(IssueCouponFilterRequestVO request, PageRequest pageable) {
        QIssueCoupon issueCoupon = QIssueCoupon.issueCoupon;
        QMember member = QMember.member;
        QCouponEntity couponEntity = QCouponEntity.couponEntity;

        BooleanBuilder builder = new BooleanBuilder()
                .and(likeCouponName(request))
                .and(likeCouponContents(request))
                .and(eqCouponCategoryName(request))
                .and(eqStudentCode(request))
                .and(likeStudentName(request))
                .and(eqCouponUseStatus(request))
                .and(betweenDiscountRate(request))
                .and(betweenCouponStartDate(request))
                .and(betweenCouponEndDate(request))
                .and(betweenCouponUseDate(request))
                .and(betweenCouponIssueDate(request));

        List<IssueCoupon> results = queryFactory
                .selectFrom(issueCoupon)
                .join(issueCoupon.coupon, couponEntity).fetchJoin()
                .join(issueCoupon.student, member).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 조회
        Long total = queryFactory
                .select(issueCoupon.count())
                .from(issueCoupon)
                .join(issueCoupon.coupon, couponEntity)
                .join(issueCoupon.student, member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }

    private BooleanExpression likeCouponName(IssueCouponFilterRequestVO request) {

        if (request.getCouponName() == null || request.getCouponName().isEmpty()) {
            return null;
        }
        return issueCoupon.coupon.couponName.likeIgnoreCase("%" + request.getCouponName() + "%");
    }

    private BooleanExpression likeCouponContents(IssueCouponFilterRequestVO request) {

        if (request.getCouponContents() == null || request.getCouponContents().isEmpty()) {
            return null;
        }
        return issueCoupon.coupon.couponContents.likeIgnoreCase("%" + request.getCouponContents() + "%");
    }

    private BooleanExpression eqCouponCategoryName(IssueCouponFilterRequestVO request) {
        String couponCategoryName = request.getCouponCategoryName();

        if (request.getCouponCategoryName() == null || request.getCouponCategoryName().isEmpty()) {
            return null;
        }

        return couponEntity.couponCategory.couponCategoryName.eq(couponCategoryName);
    }

    private BooleanExpression eqStudentCode(IssueCouponFilterRequestVO request) {
        if (request.getStudentCode() == null) {
            return null;
        }

        return member.memberCode.eq(request.getStudentCode());
    }

    private BooleanExpression likeStudentName(IssueCouponFilterRequestVO request) {
        if (request.getStudentName() == null || request.getStudentName().isEmpty()) {
            return null;
        }
        // 학생 이름을 포함하는 검색을 위해 likeIgnoreCase 사용
        return member.memberName.likeIgnoreCase("%" + request.getStudentName() + "%");
    }


    private BooleanExpression eqCouponUseStatus(IssueCouponFilterRequestVO request) {
        if (request.getCouponUseStatus() == null) {
            return null;
        }

        return issueCoupon.couponUseStatus.eq(request.getCouponUseStatus());
    }

    private BooleanExpression betweenDiscountRate(IssueCouponFilterRequestVO request) {
        if (request.getMinDiscountRate() == null && request.getMaxDiscountRate() == null) {
            return null;
        }

        if (request.getMinDiscountRate() == null) {
            return issueCoupon.coupon.couponDiscountRate.loe(request.getMaxDiscountRate());
        }

        if (request.getMaxDiscountRate() == null) {
            return issueCoupon.coupon.couponDiscountRate.goe(request.getMinDiscountRate());
        }

            return issueCoupon.coupon.couponDiscountRate.between(request.getMinDiscountRate(), request.getMaxDiscountRate());
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
            return issueCoupon.couponUseDate.loe(request.getEndCouponUseDate());
        }

        if (request.getEndCouponUseDate() == null) {
            return issueCoupon.couponUseDate.goe(request.getStartCouponUseDate());
        }

        return issueCoupon.couponUseDate.between(request.getStartCouponUseDate(), request.getEndCouponUseDate());
    }

    private BooleanExpression betweenCouponIssueDate(IssueCouponFilterRequestVO request) {

        if (request.getStartCouponIssueDate() == null && request.getEndCouponIssueDate() == null) {
            return null;
        }

        if (request.getStartCouponIssueDate() == null) {
            return issueCoupon.couponIssueDate.loe(request.getEndCouponIssueDate());
        }

        if (request.getEndCouponIssueDate() == null) {
            return issueCoupon.couponIssueDate.goe(request.getStartCouponIssueDate());
        }

        return issueCoupon.couponIssueDate.between(request.getStartCouponIssueDate(), request.getEndCouponIssueDate());
    }
}
