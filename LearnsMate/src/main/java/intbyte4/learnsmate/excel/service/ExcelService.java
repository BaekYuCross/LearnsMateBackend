package intbyte4.learnsmate.excel.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public void importDataFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String column1 = row.getCell(0).getStringCellValue();
                String column2 = row.getCell(1).getStringCellValue();

                System.out.println("컬럼1: " + column1 + ", 컬럼2: " + column2);
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel 데이터 처리 중 오류 발생", e);
        }
    }

    public void exportSelectedColumnsToExcel(OutputStream outputStream, List<String> selectedColumns) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Lecture Data");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < selectedColumns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(selectedColumns.get(i));
            }

            List<Map<String, Object>> lectureData = fetchLectureData();
            int rowIndex = 1;
            for (Map<String, Object> data : lectureData) {
                Row row = sheet.createRow(rowIndex++);
                int colIndex = 0;
                for (String column : selectedColumns) {
                    Cell cell = row.createCell(colIndex++);
                    cell.setCellValue(data.getOrDefault(column, "").toString());
                }
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류 발생", e);
        }
    }

    private List<Map<String, Object>> fetchLectureData() {

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> row1 = Map.of(
                "강의코드", "12345",
                "강의명", "유제온의 레전드 백엔드 마스터",
                "강사명", "유제온",
                "강의 상태", "제공중"
        );
        Map<String, Object> row2 = Map.of(
                "강의코드", "67890",
                "강의명", "김철수의 프론트엔드 왕초보",
                "강사명", "김철수",
                "강의 상태", "미제공"
        );
        data.add(row1);
        data.add(row2);
        return data;
    }

}
