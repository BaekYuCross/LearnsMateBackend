package intbyte4.learnsmate.blacklist.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.domain.entity.QBlacklist;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BlacklistRepositoryImpl implements BlacklistRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Blacklist> searchBy(BlacklistFilterRequestDTO request) {
        QBlacklist blacklist = QBlacklist.blacklist;

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqMemberCode(request.getMemberCode()))
                .and(likeMemberName(request.getMemberName()))
                .and(likeMemberEmail(request.getMemberEmail()));

        return queryFactory
                .selectFrom(blacklist)
                .where(builder)
                .fetch();
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
}