package intbyte4.learnsmate.member.controller;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import intbyte4.learnsmate.member.service.MemberExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/member/excel")
@Slf4j
@RequiredArgsConstructor
public class MemberExcelController {

    private final MemberExcelService memberExcelService;

    @PostMapping("/download/student")
    @Operation(summary = "학생 엑셀 다운로드", description = "학생 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadStudentExcel(HttpServletResponse response, @RequestBody(required = false) MemberFilterRequestDTO filterDTO) {
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
//                log.info("Answer status type: {}", filterDTO.getVocAnswerStatus() != null ?
//                        filterDTO.getVocAnswerStatus().getClass().getName() : "null");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"student_data.xlsx\"");

            memberExcelService.exportMemberToExcel(response.getOutputStream(), filterDTO, MemberType.STUDENT);
            log.info("Excel file successfully written to response output stream.");
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }

    @PostMapping("/download/tutor")
    @Operation(summary = "강사 엑셀 다운로드", description = "강사 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadTutorExcel(HttpServletResponse response, @RequestBody(required = false) MemberFilterRequestDTO filterDTO) {
        try {
            log.info("Excel download request received");

            if (filterDTO != null) {
                log.info("Filter DTO parsed: {}", filterDTO);
//                log.info("Answer status type: {}", filterDTO.getVocAnswerStatus() != null ?
//                        filterDTO.getVocAnswerStatus().getClass().getName() : "null");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"student_data.xlsx\"");

            memberExcelService.exportMemberToExcel(response.getOutputStream(), filterDTO, MemberType.TUTOR);
        } catch (Exception e) {
            log.error("Error during excel download:", e);
            throw new RuntimeException("Excel download failed", e);
        }
    }

    @PostMapping("/upload/student")
    @Operation(summary = "학생 엑셀 업로드", description = "엑셀 파일을 통해 학생 정보를 일괄 등록합니다.")
    public void uploadStudentExcel(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Excel upload request received for student. File name: {}", file.getOriginalFilename());

        if (!isExcelFile(file)) {
            log.error("Invalid file format: {}", file.getOriginalFilename());
            throw new CommonException(StatusEnum.INVALID_FILE_FORMAT);
        }

        memberExcelService.importMemberFromExcel(file, MemberType.STUDENT);
        log.info("good");
    }

    private boolean isExcelFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && (
                filename.endsWith(".xlsx") ||
                        filename.endsWith(".xls")
        );
    }
}

