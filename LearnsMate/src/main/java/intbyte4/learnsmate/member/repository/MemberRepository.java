package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Primary
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    // 기존 offset 기반 쿼리
    Page<Member> findByMemberType(MemberType memberType, Pageable pageable);

    @Query("SELECT m FROM member m " +
            "WHERE m.memberType = :memberType " +
            "AND m.memberCode > :memberCodeCursor " +
            "ORDER BY m.memberCode ASC")
    List<Member> findByMemberCodeLessThanAndMemberType(
            @Param("memberCodeCursor") Long memberCodeCursor,
            @Param("memberType") MemberType memberType,
            Pageable pageable
    );

    // memberType별 카운트 쿼리 추가
    long countByMemberType(MemberType memberType);

    @Query("SELECT m FROM member m WHERE m.memberType = :memberType AND (:cursor IS NULL OR m.createdAt < :cursor) ORDER BY m.createdAt DESC")
    List<Member> findMembersByCursorAndMemberType(@Param("cursor") LocalDateTime cursor, @Param("memberType") MemberType memberType, Pageable pageable);

    Member findByMemberFlagTrueAndMemberCodeAndMemberType(Long memberCode, MemberType memberType);
}
