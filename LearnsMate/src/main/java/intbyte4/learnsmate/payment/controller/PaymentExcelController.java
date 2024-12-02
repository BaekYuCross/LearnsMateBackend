package intbyte4.learnsmate.payment.controller;

import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import intbyte4.learnsmate.payment.service.PaymentExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments/excel")
@Slf4j
@RequiredArgsConstructor
public class PaymentExcelController {

    private final PaymentExcelService paymentExcelService;

    @PostMapping("/download")
    @Operation(summary = "결제 내역 엑셀 다운로드", description = "결제 내역을 엑셀 파일로 다운로드합니다.")
    public void downloadPaymentExcel(HttpServletResponse response, @RequestBody(required = false) PaymentFilterRequestVO filterDTO) {
        try {
            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"payment_data.xlsx\"");

            paymentExcelService.exportPaymentToExcel(response.getOutputStream(), filterDTO);
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }
}