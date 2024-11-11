package intbyte4.learnsmate.excel.controller;

import intbyte4.learnsmate.excel.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    // Excel 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importDataFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Excel 파일 업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Excel 파일 업로드 실패: " + e.getMessage());
        }
    }

    // Excel 파일 다운로드
    @Operation(summary = "엑셀 다운로드")
    @GetMapping("/excel/download")
    public void downloadExcel(HttpServletResponse response, @RequestParam List<String> selectedColumns) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"lecture_data.xlsx\"");

        excelService.exportSelectedColumnsToExcel(response.getOutputStream(), selectedColumns);
    }
}

