package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VOCExcelService {
    private final VOCFacade vocFacade;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("vocCode", "VOC 번호");
        put("vocContent", "VOC 내용");
        put("vocCategoryName", "카테고리");
        put("memberType", "고객 유형");
        put("memberName", "고객명");
        put("memberCode", "고객 코드");
        put("adminName", "담당자");
        put("createdAt", "등록일");
        put("vocAnswerStatus", "답변 상태");
        put("vocAnswerSatisfaction", "만족도");
    }};

    public void exportVOCToExcel(OutputStream outputStream, VOCFilterRequestDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("VOC Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            createHeader(sheet, headerStyle);

            List<ResponseFindVOCVO> vocList;
            if (filterDTO != null) {
                log.info("Applying filter with DTO: {}", filterDTO);
                log.info("Answer status filter value: {}", filterDTO.getVocAnswerStatus());
                vocList = vocFacade.findAllVOCsByFilter(filterDTO);
            } else {
                log.info("No filter applied, getting all VOCs");
                vocList = vocFacade.findAllVOCs();
            }
            log.info("Found {} VOCs to export", vocList.size());

            writeData(sheet, vocList, dateStyle);

            for (int i = 0; i < COLUMNS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create VOC Excel file", e);
            throw new RuntimeException("Failed to create VOC Excel file", e);
        }
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;
        for (String headerValue : COLUMNS.values()) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellValue(headerValue);
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeData(Sheet sheet, List<ResponseFindVOCVO> vocList, CellStyle dateStyle) {
        int rowNum = 1;
        for (ResponseFindVOCVO voc : vocList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : COLUMNS.keySet()) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, voc, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindVOCVO voc, CellStyle dateStyle) {
        switch (key) {
            case "vocCode" -> cell.setCellValue(voc.getVocCode());
            case "vocContent" -> cell.setCellValue(voc.getVocContent());
            case "vocCategoryName" -> cell.setCellValue(voc.getVocCategoryName());
            case "memberType" -> cell.setCellValue(voc.getMemberType());
            case "memberName" -> cell.setCellValue(voc.getMemberName());
            case "memberCode" -> cell.setCellValue(voc.getMemberCode() != null ?
                    String.valueOf(voc.getMemberCode()) : "-");
            case "adminName" -> cell.setCellValue(voc.getAdminName() != null ?
                    voc.getAdminName() : "-");
            case "createdAt" -> {
                if (voc.getCreatedAt() != null) {
                    cell.setCellValue(voc.getCreatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "vocAnswerStatus" -> cell.setCellValue(voc.getVocAnswerStatus() != null &&
                    voc.getVocAnswerStatus() ? "답변완료" : "미답변");
            case "vocAnswerSatisfaction" -> cell.setCellValue(voc.getVocAnswerSatisfaction() != null ?
                    voc.getVocAnswerSatisfaction() : "-");
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        return style;
    }
}