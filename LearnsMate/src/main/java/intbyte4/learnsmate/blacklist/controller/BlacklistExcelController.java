package intbyte4.learnsmate.blacklist.controller;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.service.BlacklistExcelService;
import intbyte4.learnsmate.member.domain.MemberType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklist/excel")
@Slf4j
@RequiredArgsConstructor
public class BlacklistExcelController {

    private final BlacklistExcelService blacklistExcelService;

    @PostMapping("/download/student")
    @Operation(summary = "학생 블랙리스트 엑셀 다운로드", description = "학생 블랙리스트 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadStudentBlacklistExcel(HttpServletResponse response, @RequestBody(required = false)BlacklistFilterRequestDTO filterDTO){
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
//                log.info("Answer status type: {}", filterDTO.getVocAnswerStatus() != null ?
//                        filterDTO.getVocAnswerStatus().getClass().getName() : "null");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"student_black_data.xlsx\"");

            blacklistExcelService.exportBlacklistToExcel(response.getOutputStream(), filterDTO, MemberType.STUDENT);
            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }

    @PostMapping("/download/tutor")
    @Operation(summary = "강사 블랙리스트 엑셀 다운로드", description = "강사 블랙리스트 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadTutorBlacklistExcel(HttpServletResponse response, @RequestBody(required = false)BlacklistFilterRequestDTO filterDTO){
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
//                log.info("Answer status type: {}", filterDTO.getVocAnswerStatus() != null ?
//                        filterDTO.getVocAnswerStatus().getClass().getName() : "null");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"tutor_black_data.xlsx\"");

            blacklistExcelService.exportBlacklistToExcel(response.getOutputStream(), filterDTO, MemberType.TUTOR);
            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }

}
