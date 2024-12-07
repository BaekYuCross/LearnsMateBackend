package intbyte4.learnsmate.campaign_template.controller;

import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import intbyte4.learnsmate.campaign_template.service.CampaignTemplateExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign-template/excel")
@Slf4j
@RequiredArgsConstructor
public class CampaignTemplateExcelController {

    private final CampaignTemplateExcelService campaignTemplateExcelService;

    @PostMapping("/download/campaign-templates")
    @Operation(summary = "캠페인템플릿 엑셀 다운로드", description = "캠페인템플릿 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadCampaignTemplateExcel(HttpServletResponse response, @RequestBody(required = false) CampaignTemplateFilterDTO filterDTO) {
        try {
            log.info("Excel download request received");
            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"campaign_template_data.xlsx\"");

            campaignTemplateExcelService.exportCampaignTemplateToExcel(response.getOutputStream(), filterDTO);
            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }
}
