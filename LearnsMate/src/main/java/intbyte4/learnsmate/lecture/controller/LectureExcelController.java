package intbyte4.learnsmate.lecture.controller;

import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.service.LectureExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lecture/excel")
@Slf4j
@RequiredArgsConstructor
public class LectureExcelController {

    private final LectureExcelService lectureExcelService;

    @PostMapping("/download")
    @Operation(summary = "강의 엑셀 다운로드", description = "강의 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadLectureExcel(HttpServletResponse response, @RequestBody(required = false) LectureFilterDTO filterDTO) {
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
                log.info("Lecture status type: {}", filterDTO.getLectureStatus() != null ?
                        filterDTO.getLectureStatus().getClass().getName() : "null");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"lecture_data.xlsx\"");

            lectureExcelService.exportLectureToExcel(response.getOutputStream(), filterDTO);
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }
}