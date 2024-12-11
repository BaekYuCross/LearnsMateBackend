package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import intbyte4.learnsmate.payment.repository.CustomPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentExcelService {

    private final CustomPaymentRepository paymentRepository;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("paymentCode", "결제코드");
        put("createdAt", "결제일");
        put("lectureCode", "강의코드");
        put("lectureTitle", "강의명");
        put("lectureCategoryName", "강의 카테고리");
        put("tutorCode", "강사코드");
        put("tutorName", "강사명");
        put("studentCode", "학생코드");
        put("studentName", "학생명");
        put("lecturePrice", "원가(원)");
        put("couponIssuanceName", "적용쿠폰");
        put("paymentPrice", "결제액(원)");
    }};

    private static final List<String> FIXED_COLUMN_ORDER = List.of(
            "paymentCode", "createdAt", "lectureCode", "lectureTitle",
            "lectureCategoryName", "tutorCode", "tutorName",
            "studentCode", "studentName", "lecturePrice",
            "couponIssuanceName", "paymentPrice"
    );

    public void exportPaymentToExcel(OutputStream outputStream, PaymentFilterRequestVO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payment Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            List<String> selectedColumns = filterDTO != null && filterDTO.getSelectedColumns() != null ? filterDTO.getSelectedColumns() : new ArrayList<>(COLUMNS.keySet());

            createHeader(sheet, headerStyle, selectedColumns);

            Page<PaymentFilterDTO> payments = filterDTO != null ?
                    paymentRepository.findPaymentByFilters(filterDTO, PageRequest.of(0, Integer.MAX_VALUE)) :
                    paymentRepository.findPaymentByFilters(new PaymentFilterRequestVO(), PageRequest.of(0, Integer.MAX_VALUE));

            writeData(sheet, payments.getContent(), dateStyle, selectedColumns);

            for (int i = 0; i < selectedColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Payment Excel file", e);
            throw new RuntimeException("Failed to create Payment Excel file", e);
        }
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle, List<String> selectedColumns) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;

        for (String columnKey : FIXED_COLUMN_ORDER) {
            if (selectedColumns.contains(columnKey)) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(COLUMNS.get(columnKey));
                cell.setCellStyle(headerStyle);
            }
        }
    }

    private void writeData(Sheet sheet, List<PaymentFilterDTO> paymentList, CellStyle dateStyle, List<String> selectedColumns) {
        int rowNum = 1;

        for (PaymentFilterDTO payment : paymentList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;

            for (String key : FIXED_COLUMN_ORDER) {
                if (selectedColumns.contains(key)) {
                    Cell cell = row.createCell(columnIndex++);
                    setValueByColumnKey(cell, key, payment, dateStyle);
                }
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, PaymentFilterDTO payment, CellStyle dateStyle) {
        switch (key) {
            case "paymentCode" -> cell.setCellValue(payment.getPaymentCode());
            case "createdAt" -> {
                if (payment.getCreatedAt() != null) {
                    cell.setCellValue(payment.getCreatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "lectureCode" -> cell.setCellValue(payment.getLectureCode());
            case "lectureTitle" -> cell.setCellValue(payment.getLectureTitle());
            case "lectureCategoryName" -> cell.setCellValue(payment.getLectureCategoryName());
            case "tutorCode" -> cell.setCellValue(payment.getTutorCode() != null ?
                    String.valueOf(payment.getTutorCode()) : "-");
            case "tutorName" -> cell.setCellValue(payment.getTutorName());
            case "studentCode" -> cell.setCellValue(payment.getStudentCode() != null ?
                    String.valueOf(payment.getStudentCode()) : "-");
            case "studentName" -> cell.setCellValue(payment.getStudentName());
            case "lecturePrice" -> cell.setCellValue(payment.getLecturePrice() != null ?
                    String.format("%,d원", payment.getLecturePrice()) : "-");
            case "couponIssuanceName" -> cell.setCellValue(payment.getCouponIssuanceName() != null ?
                    payment.getCouponIssuanceName() : "-");
            case "paymentPrice" -> cell.setCellValue(payment.getPaymentPrice() != null ?
                    String.format("%,d원", payment.getPaymentPrice()) : "-");
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
