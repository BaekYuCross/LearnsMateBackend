package intbyte4.learnsmate.coupon.controller;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import intbyte4.learnsmate.coupon.service.CouponExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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


    @PostMapping("/upload/target-coupon")
    @Operation(summary = "타겟 쿠폰 엑셀 업로드", description = "엑셀 파일을 통해 쿠폰 정보를 일괄 등록합니다.")
    public List<CouponFindResponseVO> uploadTargetCouponExcel(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("쿠폰 파일 업로드 시작. 파일 이름은 : {}", file.getOriginalFilename());

        if (!isExcelFile(file)) {
            log.error("Invalid file format: {}", file.getOriginalFilename());
            throw new CommonException(StatusEnum.INVALID_FILE_FORMAT);
        }

        // 유효한 타겟 유저 리스트 반환
        return couponExcelService.importTargetCouponFromExcel(file);
    }


    private boolean isExcelFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && (
                filename.endsWith(".xlsx") ||
                        filename.endsWith(".xls")
        );
    }
}
