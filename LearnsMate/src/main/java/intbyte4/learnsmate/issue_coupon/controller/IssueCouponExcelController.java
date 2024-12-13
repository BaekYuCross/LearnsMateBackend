package intbyte4.learnsmate.issue_coupon.controller;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issue-coupon/excel")
@Slf4j
@RequiredArgsConstructor
public class IssueCouponExcelController {

    private final IssueCouponExcelService issueCouponExcelService;

    @PostMapping("/download")
    @Operation(summary = "발급쿠폰 엑셀 다운로드", description = "발급쿠폰 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadStudentExcel(HttpServletResponse response, @RequestBody(required = false) IssuedCouponFilterDTO filterDTO) {
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"coupon_data.xlsx\"");

            issueCouponExcelService.exportCouponToExcel(response.getOutputStream(), filterDTO);

            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }



}
