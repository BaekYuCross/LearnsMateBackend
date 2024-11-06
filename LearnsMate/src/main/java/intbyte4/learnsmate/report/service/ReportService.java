package intbyte4.learnsmate.report.service;

import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.entity.Report;
import intbyte4.learnsmate.report.mapper.ReportMapper;
import intbyte4.learnsmate.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    // 모든 신고 조회
    public List<ReportDTO> findAllReport() {

        List<Report> reportList = reportRepository.findAll();
        // dto로 변환.
        List<ReportDTO> reportDTOList = new ArrayList<>();
        for (Report report : reportList) {
            reportDTOList.add(reportMapper.fromReportToReportDTO(report));
        }

        return reportDTOList;
    }
}
