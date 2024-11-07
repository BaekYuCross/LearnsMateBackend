package intbyte4.learnsmate.report.repository;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import intbyte4.learnsmate.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByReportedMember_MemberCode(Long memberCode);

    @Query("SELECT new intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO(r.reportedMember, COUNT(r)) " +
            "FROM report r GROUP BY r.reportedMember HAVING COUNT(r) >= 5")
    List<ReportedMemberDTO> findMembersWithReportsCountGreaterThanEqualFive();
}
