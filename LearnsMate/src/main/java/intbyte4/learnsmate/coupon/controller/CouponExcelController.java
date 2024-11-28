package intbyte4.learnsmate.coupon.controller;

import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.service.CouponExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon/excel")
@Slf4j
@RequiredArgsConstructor
public class CouponExcelController {

    private final CouponExcelService couponExcelService;

    @PostMapping("/download")
    @Operation(summary = "쿠폰 엑셀 다운로드", description = "쿠폰 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadStudentExcel(HttpServletResponse response, @RequestBody(required = false) CouponFilterDTO filterDTO) {
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
//                log.info("Answer status type: {}", filterDTO.getVocAnswerStatus() != null ?
//                        filterDTO.getVocAnswerStatus().getClass().getName() : "null");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"coupon_data.xlsx\"");

            couponExcelService.exportCouponToExcel(response.getOutputStream(), filterDTO);

            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }

}
