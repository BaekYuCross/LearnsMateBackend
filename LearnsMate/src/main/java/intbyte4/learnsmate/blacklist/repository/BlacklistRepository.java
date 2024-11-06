package intbyte4.learnsmate.blacklist.repository;

import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.member.domain.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    @Query("SELECT b FROM blacklist b " +
            "JOIN Member m ON b.member.memberCode = m.memberCode " +
            "WHERE m.memberType = :memberType")
    List<Blacklist> findAllBlacklistByMemberType(MemberType memberType);
}
