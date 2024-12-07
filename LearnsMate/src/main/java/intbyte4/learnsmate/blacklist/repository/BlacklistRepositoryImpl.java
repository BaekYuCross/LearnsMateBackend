package intbyte4.learnsmate.blacklist.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.domain.entity.QBlacklist;
import intbyte4.learnsmate.member.domain.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BlacklistRepositoryImpl implements BlacklistRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Blacklist> searchBy(BlacklistFilterRequestDTO request, Pageable pageable) {
        QBlacklist blacklist = QBlacklist.blacklist;
        BooleanBuilder builder = new BooleanBuilder();
        if (eqBlackCode(request.getBlackCode()) != null) {
            builder.and(eqBlackCode(request.getBlackCode()));
        }
        if (eqMemberCode(request.getMemberCode()) != null) {
            builder.and(eqMemberCode(request.getMemberCode()));
        }
        if (likeMemberName(request.getMemberName()) != null) {
            builder.and(likeMemberName(request.getMemberName()));
        }
        if (likeMemberEmail(request.getMemberEmail()) != null) {
            builder.and(likeMemberEmail(request.getMemberEmail()));
        }
        if (eqMemberType(request.getMemberType()) != null) {
            builder.and(eqMemberType(request.getMemberType()));
        }

        // Query 생성
        JPAQuery<Blacklist> query = queryFactory
                .selectFrom(blacklist)
                .where(builder)
                .orderBy(blacklist.blackCode.desc());

        long total = query.fetchCount();

        List<Blacklist> blacklists = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(blacklists, pageable, total);
    }

    @Override
    public List<Blacklist> searchByWithoutPaging(BlacklistFilterRequestDTO request) {
        QBlacklist blacklist = QBlacklist.blacklist;
        BooleanBuilder builder = new BooleanBuilder();

        // 각 필터 조건 추가
        if (eqBlackCode(request.getBlackCode()) != null) {
            builder.and(eqBlackCode(request.getBlackCode()));
        }
        if (eqMemberCode(request.getMemberCode()) != null) {
            builder.and(eqMemberCode(request.getMemberCode()));
        }
        if (likeMemberName(request.getMemberName()) != null) {
            builder.and(likeMemberName(request.getMemberName()));
        }
        if (likeMemberEmail(request.getMemberEmail()) != null) {
            builder.and(likeMemberEmail(request.getMemberEmail()));
        }
        if (eqMemberType(request.getMemberType()) != null) {
            builder.and(eqMemberType(request.getMemberType()));
        }

        // 쿼리 실행
        return queryFactory
                .selectFrom(blacklist)
                .join(blacklist.member).fetchJoin()  // N+1 문제 방지를 위한 fetch join
                .where(builder)
                .orderBy(blacklist.createdAt.desc())  // 생성일 기준 내림차순 정렬
                .fetch();
    }

    private BooleanExpression eqBlackCode(Long blackCode){
        return blackCode == null ? null : QBlacklist.blacklist.blackCode.eq(blackCode);
    }

    // memberCode 검색 조건
    private BooleanExpression eqMemberCode(Long memberCode) {
        return memberCode == null ? null : QBlacklist.blacklist.member.memberCode.eq(memberCode);
    }

    // memberName 검색 조건
    private BooleanExpression likeMemberName(String memberName) {
        return memberName == null ? null : QBlacklist.blacklist.member.memberName.containsIgnoreCase(memberName);
    }

    // memberEmail 검색 조건
    private BooleanExpression likeMemberEmail(String memberEmail) {
        return memberEmail == null ? null : QBlacklist.blacklist.member.memberEmail.containsIgnoreCase(memberEmail);
    }

    // memberType 검색 조건 (학생 또는 강사)
    private BooleanExpression eqMemberType(MemberType memberType) {
        return memberType == null ? null : QBlacklist.blacklist.member.memberType.eq(memberType);
    }
}