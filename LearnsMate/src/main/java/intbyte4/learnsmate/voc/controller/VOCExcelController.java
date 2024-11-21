package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.service.VOCExcelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/voc/excel")
@Slf4j
@RequiredArgsConstructor
public class VOCExcelController {

    private final VOCExcelService vocExcelService;

    @PostMapping("/download")
    @Operation(summary = "VOC 엑셀 다운로드", description = "VOC 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadVOCExcel(HttpServletResponse response, @RequestBody(required = false) VOCFilterRequestDTO filterDTO, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"voc_data.xlsx\"");

        vocExcelService.exportVOCToExcel(response.getOutputStream(), filterDTO, page, size);
    }
}
