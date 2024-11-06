package intbyte4.learnsmate.report.mapper;

import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.entity.Report;
import intbyte4.learnsmate.report.domain.vo.response.ResponseFindReportVO;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportDTO fromReportToReportDTO(Report report) {
        return ReportDTO.builder()
                .reportCode(report.getReportCode())
                .reportReason(report.getReportReason())
                .reportDate(report.getReportDate())
                .commentCode(report.getComment().getCommentCode())
                .reportMemberCode(report.getReportMember().getMemberCode())
                .reportedMemberCode(report.getReportedMember().getMemberCode())
                .build();
    }

    // dto -> ResponseFindReportVO
    public ResponseFindReportVO fromReportDTOToResponseFindReportVO(ReportDTO reportDTO) {
        return ResponseFindReportVO.builder()
                .reportCode(reportDTO.getReportCode())
                .reportReason(reportDTO.getReportReason())
                .reportDate(reportDTO.getReportDate())
                .commentCode(reportDTO.getCommentCode())
                .reportMemberCode(reportDTO.getReportMemberCode())
                .reportedMemberCode(reportDTO.getReportedMemberCode())
                .build();
    }
}
