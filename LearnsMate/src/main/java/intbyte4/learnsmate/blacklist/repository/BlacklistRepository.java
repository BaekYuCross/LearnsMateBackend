package intbyte4.learnsmate.blacklist.repository;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.member.domain.MemberType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface BlacklistRepository extends JpaRepository<Blacklist, Long>, BlacklistRepositoryCustom {

    @Query("SELECT new intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO(" +
            "b.blackCode, m.memberCode, m.memberName, m.memberEmail, b.blackReason, b.createdAt, a.adminCode, a.adminName" +
            ") " +
            "FROM blacklist b " +
            "JOIN b.member m " +
            "LEFT JOIN b.admin a " +
            "WHERE m.memberType = :memberType")
    List<BlacklistDTO> findAllBlacklistByMemberType(@Param("memberType") MemberType memberType);
}
