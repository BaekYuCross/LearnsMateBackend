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

    Page<Member> findByMemberFlagTrueAndMemberType(MemberType memberType, PageRequest pageable);

    @Query("SELECT m FROM member m WHERE m.memberType = :memberType AND (:cursor IS NULL OR m.createdAt < :cursor) ORDER BY m.createdAt DESC")
    List<Member> findMembersByCursorAndMemberType(@Param("cursor") LocalDateTime cursor, @Param("memberType") MemberType memberType, Pageable pageable);

    Member findByMemberFlagTrueAndMemberCodeAndMemberType(Long memberCode, MemberType memberType);
}
