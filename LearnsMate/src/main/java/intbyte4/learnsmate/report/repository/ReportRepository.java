package intbyte4.learnsmate.report.repository;

import intbyte4.learnsmate.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
