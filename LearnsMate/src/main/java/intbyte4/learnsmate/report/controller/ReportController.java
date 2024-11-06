package intbyte4.learnsmate.report.controller;

import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.vo.response.ResponseFindReportVO;
import intbyte4.learnsmate.report.mapper.ReportMapper;
import intbyte4.learnsmate.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportController(ReportService reposrtService, ReportMapper reportMapper) {
        this.reportService = reposrtService;
        this.reportMapper = reportMapper;
    }

    // 1. 모든 신고 내역 조회
    public ResponseEntity<List<ResponseFindReportVO>> findAllReport(){

        List<ReportDTO> reportDTOList = reportService.findAllReport();

        List<ResponseFindReportVO> responseList = new ArrayList<>();
        for(ReportDTO reportDTO : reportDTOList){
            // dto -> vo 메서드
            responseList.add(reportMapper.fromReportDTOToResponseFindReportVO(reportDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
