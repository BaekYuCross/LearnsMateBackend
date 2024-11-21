package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCPageResponse;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VOCExcelService {

    private final VOCFacade vocFacade;

    private static final Map<String, String> COLUMN_NAMES = Map.of(
            "vocCode", "VOC 번호",
            "vocContent", "VOC 내용",
            "vocCategoryName", "카테고리",
            "memberType", "고객 유형",
            "memberName", "고객명",
            "memberCode", "고객 코드",
            "adminName", "담당자",
            "createdAt", "등록일",
            "vocAnswerStatus", "답변 상태",
            "vocAnswerSatisfaction", "만족도"
    );

    public void exportVOCToExcel(OutputStream outputStream, VOCFilterRequestDTO filterDTO, int page, int size) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("VOC Data");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            createHeader(sheet, headerStyle);

            VOCPageResponse<ResponseFindVOCVO> response;
            if (filterDTO != null) {
                response = vocFacade.filterVOCsByPage(filterDTO, page, size);
            } else {
                response = vocFacade.findVOCsByPage(page, size);
            }
            writeData(sheet, response.getContent(), dateStyle);

            for (int i = 0; i < COLUMN_NAMES.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create VOC Excel file", e);
        }
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;

        for (Map.Entry<String, String> entry : COLUMN_NAMES.entrySet()) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellValue(entry.getValue());
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeData(Sheet sheet, List<ResponseFindVOCVO> vocList, CellStyle dateStyle) {
        int rowNum = 1;
        for (ResponseFindVOCVO voc : vocList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;

            createCell(row, columnIndex++, voc.getVocCode());
            createCell(row, columnIndex++, voc.getVocContent());
            createCell(row, columnIndex++, voc.getVocCategoryName());
            createCell(row, columnIndex++, voc.getMemberType());
            createCell(row, columnIndex++, voc.getMemberName());
            createCell(row, columnIndex++, voc.getMemberCode() != null ? String.valueOf(voc.getMemberCode()) : "");
            createCell(row, columnIndex++, voc.getAdminName());
            Cell dateCell = row.createCell(columnIndex++);
            if (voc.getCreatedAt() != null) {
                dateCell.setCellValue(voc.getCreatedAt());
                dateCell.setCellStyle(dateStyle);
            }
            createCell(row, columnIndex++, voc.getVocAnswerStatus() != null &&
                    voc.getVocAnswerStatus() ? "답변완료" : "미답변");
            createCell(row, columnIndex, voc.getVocAnswerSatisfaction() != null ?
                    voc.getVocAnswerSatisfaction() : "-");
        }
    }

    private void createCell(Row row, int columnIndex, String value) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value : "-");
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