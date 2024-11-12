package intbyte4.learnsmate.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.entity.QMember;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> searchBy(Member member,
                                 LocalDateTime birthStartDate, LocalDateTime birthEndDate,
                                 LocalDateTime createdStartDate, LocalDateTime createdEndDate,
                                 LocalDateTime updatedStartDate, LocalDateTime updatedEndDate) {
        QMember qMember = QMember.member;

        return queryFactory
                .selectFrom(qMember)
                .where(
                        searchByMemberCode(member),
                        searchByMemberType(member),
                        searchByEmail(member),
                        searchByPassword(member), // 보통 검색 조건으로 사용하지 않음
                        searchByName(member),
                        searchByAge(member),
                        searchByPhone(member),
                        searchByAddress(member),
                        searchByBirth(birthStartDate, birthEndDate),          // 생년월일 범위 조건
                        searchByCreatedAt(createdStartDate, createdEndDate), // 생성일 범위 조건
                        searchByUpdatedAt(updatedStartDate, updatedEndDate)  // 수정일 범위 조건
                )
                .fetch();
    }

    // memberCode 검색 조건 (변동 없음)
    private BooleanBuilder searchByMemberCode(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberCode() != null) {
            builder.and(QMember.member.memberCode.eq(member.getMemberCode()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // memberType 검색 조건 (변동 없음)
    private BooleanBuilder searchByMemberType(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberType() != null) {
            builder.and(QMember.member.memberType.eq(member.getMemberType()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 이메일 검색 조건 (변동 없음)
    private BooleanBuilder searchByEmail(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberEmail() != null) {
            builder.and(QMember.member.memberEmail.containsIgnoreCase(member.getMemberEmail()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 비밀번호 검색 조건 (변동 없음)
    private BooleanBuilder searchByPassword(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberPassword() != null) {
            builder.and(QMember.member.memberPassword.eq(member.getMemberPassword()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 이름 검색 조건 (변동 없음)
    private BooleanBuilder searchByName(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberName() != null) {
            builder.and(QMember.member.memberName.containsIgnoreCase(member.getMemberName()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 나이 검색 조건 (변동 없음)
    private BooleanBuilder searchByAge(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberAge() != null) {
            builder.and(QMember.member.memberAge.eq(member.getMemberAge()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 연락처 검색 조건 (변동 없음)
    private BooleanBuilder searchByPhone(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberPhone() != null) {
            builder.and(QMember.member.memberPhone.containsIgnoreCase(member.getMemberPhone()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 주소 검색 조건 (변동 없음)
    private BooleanBuilder searchByAddress(Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (member.getMemberAddress() != null) {
            builder.and(QMember.member.memberAddress.containsIgnoreCase(member.getMemberAddress()));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 생년월일 검색 조건 (범위 검색 - 수정됨)
    private BooleanBuilder searchByBirth(LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        if (startDate != null) {
            builder.and(QMember.member.memberBirth.goe(startDate));
        }
        if (endDate != null) {
            builder.and(QMember.member.memberBirth.loe(endDate));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 생성일 범위 검색 조건 (변동 없음)
    private BooleanBuilder searchByCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        if (startDate != null) {
            builder.and(QMember.member.createdAt.goe(startDate));
        }
        if (endDate != null) {
            builder.and(QMember.member.createdAt.loe(endDate));
        }
        return builder; // 더이상 변동내용 없음.
    }

    // 수정일 범위 검색 조건 (변동 없음)
    private BooleanBuilder searchByUpdatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        if (startDate != null) {
            builder.and(QMember.member.updatedAt.goe(startDate));
        }
        if (endDate != null) {
            builder.and(QMember.member.updatedAt.loe(endDate));
        }
        return builder; // 더이상 변동내용 없음.
    }
}
