package intbyte4.learnsmate.report.controller;

import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.vo.response.ResponseFindReportVO;
import intbyte4.learnsmate.report.mapper.ReportMapper;
import intbyte4.learnsmate.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 1. 모든 신고 내역 조회 // /report
    @GetMapping
    public ResponseEntity<List<ResponseFindReportVO>> findAllReport(){

        List<ReportDTO> reportDTOList = reportService.findAllReport();

        List<ResponseFindReportVO> responseList = new ArrayList<>();
        for(ReportDTO reportDTO : reportDTOList){
            // dto -> vo 메서드
            responseList.add(reportMapper.fromReportDTOToResponseFindReportVO(reportDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    // 2. 특정 신고 내역 조회하기

//    // . 특정 멤버가 신고당한 횟수 구하기 (파라미터 -> memberCode -> vo로 담아서 보내야하는가?)
//    -> 서비스코드만 있으면 될거같다!
//    public ResponseEntity<Integer> findReportByMemberCode(@PathVariable Long memberCode){
//
//        ReportDTO reportDTO = new ReportDTO();
//        reportDTO.setReportedMemberCode(memberCode);
//
//        reportService.findCountReportedByMemberCode(reportDTO);
//
//    }
}
