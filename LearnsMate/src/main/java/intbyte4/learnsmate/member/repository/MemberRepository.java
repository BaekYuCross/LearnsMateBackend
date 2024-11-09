package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByMemberFlagTrueAndMemberType(MemberType memberType);

    Optional<String> findMemberNameByMemberCode(Long memberCode);

    Member finByMemberFlagTrueAndMemberCode(Long memberCode);
}
