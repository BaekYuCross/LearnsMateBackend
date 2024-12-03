package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
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

    @Query("SELECT m from member m WHERE m.memberType = :memberType ORDER BY m.memberCode DESC ")
    Page<Member> findByMemberType(MemberType memberType, Pageable pageable);

    Member findByMemberCodeAndMemberType(Long memberCode, MemberType memberType);

    @Query("SELECT m FROM member m WHERE m.memberType = :memberType ORDER BY m.memberCode DESC")
    List<Member> findAllByMemberTypeWithExcel(@Param("memberType") MemberType memberType);

    Member findByMemberEmail(String memberEmail);

    @Query("SELECT m FROM member m JOIN userPerCampaign upc ON m.memberCode = upc.student.memberCode WHERE upc.campaign.campaignCode = :campaignCode")
    Page<Member> findMembersByCampaignCode(@Param("campaignCode") Long campaignCode, Pageable pageable);

    int countByMemberTypeAndCreatedAtBetween(MemberType memberType, LocalDateTime start, LocalDateTime end);

    int countByMemberType(MemberType memberType);
}
