package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
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
public class LectureExcelService {

    private final LectureFacade lectureFacade;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("lectureCode", "강의 코드");
        put("lectureTitle", "강의명");
        put("lectureCategoryName", "카테고리");
        put("lectureLevel", "난이도");
        put("tutorName", "강사명");
        put("tutorCode", "강사 코드");
        put("price", "가격");
        put("createdAt", "등록일");
        put("lectureConfirmStatus", "승인 상태");
        put("lectureStatus", "강의 상태");
    }};

    public void exportLectureToExcel(OutputStream outputStream, LectureFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Lecture Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            createHeader(sheet, headerStyle);

            List<ResponseFindLectureVO> lectureList;
            if (filterDTO != null) {
                log.info("Applying filter with DTO: {}", filterDTO);
                lectureList = lectureFacade.findAllLecturesByFilter(filterDTO);
            } else {
                log.info("No filter applied, getting all Lectures");
                lectureList = lectureFacade.findAllLectures();
            }
            log.info("Found {} Lectures to export", lectureList.size());

            writeData(sheet, lectureList, dateStyle);

            for (int i = 0; i < COLUMNS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Lecture Excel file", e);
            throw new RuntimeException("Failed to create Lecture Excel file", e);
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

    private void writeData(Sheet sheet, List<ResponseFindLectureVO> lectureList, CellStyle dateStyle) {
        int rowNum = 1;
        for (ResponseFindLectureVO lecture : lectureList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : COLUMNS.keySet()) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, lecture, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindLectureVO lecture, CellStyle dateStyle) {
        switch (key) {
            case "lectureCode" -> cell.setCellValue(lecture.getLectureCode());
            case "lectureTitle" -> cell.setCellValue(lecture.getLectureTitle());
            case "lectureCategoryName" -> cell.setCellValue(lecture.getLectureCategoryName());
            case "lectureLevel" -> cell.setCellValue(lecture.getLectureLevel().toString());
            case "tutorName" -> cell.setCellValue(lecture.getTutorName());
            case "tutorCode" -> cell.setCellValue(lecture.getTutorCode() != null ?
                    String.valueOf(lecture.getTutorCode()) : "-");
            case "price" -> cell.setCellValue(lecture.getLecturePrice() != null ?
                    String.format("%,d원", lecture.getLecturePrice()) : "-");
            case "createdAt" -> {
                if (lecture.getCreatedAt() != null) {
                    cell.setCellValue(lecture.getCreatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "lectureConfirmStatus" -> cell.setCellValue(lecture.getLectureConfirmStatus() != null &&
                    lecture.getLectureConfirmStatus() ? "승인완료" : "미승인");
            case "lectureStatus" -> cell.setCellValue(lecture.getLectureStatus() != null &&
                    lecture.getLectureStatus() ? "운영중" : "종료");
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
