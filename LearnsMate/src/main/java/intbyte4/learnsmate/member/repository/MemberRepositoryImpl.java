package intbyte4.learnsmate.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.entity.QMember;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchBy(MemberFilterRequestDTO request, Pageable pageable) {
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqMemberCode(request.getMemberCode()))
                .and(eqMemberType(request.getMemberType()))
                .and(likeEmail(request.getMemberEmail()))
                .and(likeName(request.getMemberName()))
                .and(betweenAge(request.getMemberStartAge(), request.getMemberEndAge()))
                .and(likePhone(request.getMemberPhone()))
                .and(likeAddress(request.getMemberAddress()))
                .and(eqMemberFlag(request.getMemberFlag()))
                .and(eqMemberDormantFlag(request.getMemberDormantFlag()))
                .and(betweenBirth(request.getBirthStartDate(), request.getBirthEndDate()))
                .and(betweenCreatedAt(request.getCreatedStartDate(), request.getCreatedEndDate()));

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(member, pageable.getSort());

        // Query 생성 및 정렬 조건 적용
        JPAQuery<Member> query = queryFactory
                .selectFrom(member)
                .where(builder);

        // 정렬 조건이 있으면 적용
        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        }

        long total = query.fetch().size();

        List<Member> members = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(members, pageable, total);
    }


    @Override
    public List<Member> searchByWithoutPaging(MemberFilterRequestDTO request) {
        QMember member = QMember.member;

        // 기존 BooleanBuilder 재사용
        BooleanBuilder builder = new BooleanBuilder()
                .and(eqMemberCode(request.getMemberCode()))
                .and(eqMemberType(request.getMemberType()))
                .and(likeEmail(request.getMemberEmail()))
                .and(likeName(request.getMemberName()))
                .and(betweenAge(request.getMemberStartAge(), request.getMemberEndAge()))
                .and(likePhone(request.getMemberPhone()))
                .and(likeAddress(request.getMemberAddress()))
                .and(eqMemberFlag(request.getMemberFlag()))
                .and(eqMemberDormantFlag(request.getMemberDormantFlag()))
                .and(betweenBirth(request.getBirthStartDate(), request.getBirthEndDate()))
                .and(betweenCreatedAt(request.getCreatedStartDate(), request.getCreatedEndDate()));

        // 페이징 없이 전체 데이터 조회
        return queryFactory
                .selectFrom(member)
                .where(builder)
                .orderBy(member.createdAt.desc())
                .fetch();
    }

    // 정렬 조건 생성 메서드
    private OrderSpecifier<?> getOrderSpecifier(QMember member, Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            Sort.Order order = pageable.getSort().iterator().next();
            PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, "member");

            return new OrderSpecifier(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty())
            );
        }

        // 기본 정렬 (memberCode DESC)
        return member.memberCode.desc();
    }

    // memberCode 검색 조건
    private BooleanExpression eqMemberCode(Long memberCode) {
        return memberCode == null ? null : QMember.member.memberCode.eq(memberCode);
    }

    // memberType 검색 조건
    private BooleanExpression eqMemberType(MemberType memberType) {
        return memberType == null ? null : QMember.member.memberType.eq(memberType);
    }

    // 이메일 검색 조건
    private BooleanExpression likeEmail(String email) {
        return email == null ? null : QMember.member.memberEmail.containsIgnoreCase(email);
    }

    // 이름 검색 조건
    private BooleanExpression likeName(String name) {
        return name == null ? null : QMember.member.memberName.containsIgnoreCase(name);
    }

    // 나이 범위 검색 조건
    private BooleanExpression betweenAge(Integer startAge, Integer endAge) {
        if (startAge == null && endAge == null) return null;
        if (startAge == null) return QMember.member.memberAge.loe(endAge);
        if (endAge == null) return QMember.member.memberAge.goe(startAge);
        return QMember.member.memberAge.between(startAge, endAge);
    }

    // 연락처 검색 조건
    private BooleanExpression likePhone(String phone) {
        return phone == null ? null : QMember.member.memberPhone.containsIgnoreCase(phone);
    }

    // 주소 검색 조건
    private BooleanExpression likeAddress(String address) {
        return address == null ? null : QMember.member.memberAddress.containsIgnoreCase(address);
    }

    // 멤버 플래그 검색 조건
    private BooleanExpression eqMemberFlag(Boolean memberFlag) {
        return memberFlag == null ? null : QMember.member.memberFlag.eq(memberFlag);
    }

    // 휴면 멤버 플래그 검색 조건
    private BooleanExpression eqMemberDormantFlag(Boolean memberDormantFlag) {
        return memberDormantFlag == null ? null : QMember.member.memberDormantStatus.eq(memberDormantFlag);
    }

    // 생년월일 범위 검색 조건
    private BooleanExpression betweenBirth(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) return null;
        if (startDate == null) return QMember.member.memberBirth.loe(endDate);
        if (endDate == null) return QMember.member.memberBirth.goe(startDate);
        return QMember.member.memberBirth.between(startDate, endDate);
    }

    // 생성일 범위 검색 조건
    private BooleanExpression betweenCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) return null;
        if (startDate == null) return QMember.member.createdAt.loe(endDate);
        if (endDate == null) return QMember.member.createdAt.goe(startDate);
        return QMember.member.createdAt.between(startDate, endDate);
    }

    private OrderSpecifier<?> getOrderSpecifier(QMember member, Sort sort) {
        if (sort.isEmpty()) {
            return member.memberCode.desc(); // 기본 정렬
        }

        Sort.Order order = sort.iterator().next();

        switch (order.getProperty()) {
            case "memberCode":
                return order.isAscending() ? member.memberCode.asc() : member.memberCode.desc();
            case "memberName":
                return order.isAscending() ? member.memberName.asc() : member.memberName.desc();
            case "memberEmail":
                return order.isAscending() ? member.memberEmail.asc() : member.memberEmail.desc();
            case "memberPhone":
                return order.isAscending() ? member.memberPhone.asc() : member.memberPhone.desc();
            case "memberAddress":
                return order.isAscending() ? member.memberAddress.asc() : member.memberAddress.desc();
            case "memberAge":
                return order.isAscending() ? member.memberAge.asc() : member.memberAge.desc();
            case "memberBirth":
                return order.isAscending() ? member.memberBirth.asc() : member.memberBirth.desc();
            case "memberFlag":
                return order.isAscending() ? member.memberFlag.asc() : member.memberFlag.desc();
            case "createdAt":
                return order.isAscending() ? member.createdAt.asc() : member.createdAt.desc();
            case "updatedAt":
                return order.isAscending() ? member.updatedAt.asc() : member.updatedAt.desc();
            case "memberType":
                return order.isAscending() ? member.memberType.asc() : member.memberType.desc();
            case "memberDormantStatus":
                return order.isAscending() ? member.memberDormantStatus.asc() : member.memberDormantStatus.desc();
            case "memberPassword":
                return order.isAscending() ? member.memberPassword.asc() : member.memberPassword.desc();
            default:
                return member.memberCode.desc();
        }
    }
}
