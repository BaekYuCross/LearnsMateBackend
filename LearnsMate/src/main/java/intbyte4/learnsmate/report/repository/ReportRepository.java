package intbyte4.learnsmate.report.repository;

import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import intbyte4.learnsmate.report.domain.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByReportedMember_MemberCode(Long memberCode);

    @Query("SELECT new intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO(r.reportedMember, COUNT(r)) " +
            "FROM report r GROUP BY r.reportedMember HAVING COUNT(r) >= 5")
    Page<ReportedMemberDTO> findMembersWithReportsCountGreaterThanEqualFive(Pageable pageable);

    // 신고 당한 멤버의 멤버코드가 파라미터와 일치하는 신고 리스트
    List<Report> findByReportedMember_MemberCode(Long memberCode);
}
