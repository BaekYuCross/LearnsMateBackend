package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.AllIssuedCouponResponseVO;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueCouponExcelService {

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("couponIssuanceCode", "발급 쿠폰 번호");
        put("couponName", "쿠폰 이름");
        put("couponContents", "쿠폰 내용");
        put("couponCategoryName", "쿠폰 종류");
        put("studentCode", "고객 코드");
        put("studentName", "고객 명");
        put("couponUseStatus", "사용 여부");
        put("couponDiscountRate", "할인율");
        put("couponStartDate", "시작일");
        put("couponExpireDate", "만료일");
        put("couponIssueDate", "발급일");
        put("couponUseDate", "사용일");
    }};

    private final IssueCouponFacade issueCouponFacade;

    public void exportCouponToExcel(ServletOutputStream outputStream, IssuedCouponFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Issued Coupon Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            List<String> selectedColumns = filterDTO != null && filterDTO.getSelectedColumns() != null
                    ? filterDTO.getSelectedColumns()
                    : new ArrayList<>(COLUMNS.keySet());

            createHeader(sheet, headerStyle, selectedColumns);

            List<AllIssuedCouponResponseVO> issueCouponList;
            if (filterDTO != null) {
                log.info("Applying filter with DTO: {}", filterDTO);

                issueCouponList = issueCouponFacade.filterIssuedCoupon(filterDTO);
            } else {
                log.info("No filter applied, getting all Issued Coupons");
                issueCouponList = issueCouponFacade.findAllIssuedCoupons();
            }
            log.info("Found {} Issued Coupons to export", issueCouponList.size());

            writeData(sheet, issueCouponList, dateStyle, selectedColumns);

            for (int i = 0; i < selectedColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Issued Coupon Excel file", e);
            throw new RuntimeException("Failed to create Issued Coupon Excel file", e);
        }
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle, List<String> selectedColumns) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;
        for (String columnKey : selectedColumns) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellValue(COLUMNS.get(columnKey));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeData(Sheet sheet, List<AllIssuedCouponResponseVO> couponList, CellStyle dateStyle, List<String> selectedColumns) {
        int rowNum = 1;
        for (AllIssuedCouponResponseVO coupon : couponList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : selectedColumns) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, coupon, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, AllIssuedCouponResponseVO coupon, CellStyle dateStyle) {
        switch (key) {
            case "couponIssuanceCode" -> cell.setCellValue(coupon.getCouponIssuanceCode());
            case "couponName" -> cell.setCellValue(coupon.getCouponName());
            case "couponContents" -> cell.setCellValue(coupon.getCouponContents());
            case "couponCategoryName" -> cell.setCellValue(coupon.getCouponCategoryName());
            case "studentCode" -> cell.setCellValue(coupon.getStudentCode());
            case "studentName" -> cell.setCellValue(coupon.getStudentName());
            case "couponUseStatus" -> cell.setCellValue(coupon.getCouponUseStatus() ? "사용" : "미사용");
            case "couponDiscountRate" -> cell.setCellValue(coupon.getCouponDiscountRate() + "%");
            case "couponStartDate" -> {
                if (coupon.getCouponStartDate() != null) {
                    cell.setCellValue(coupon.getCouponStartDate());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "couponExpireDate" -> {
                if (coupon.getCouponExpireDate() != null) {
                    cell.setCellValue(coupon.getCouponExpireDate());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "couponIssueDate" -> {
                if (coupon.getCouponIssueDate() != null) {
                    cell.setCellValue(coupon.getCouponIssueDate());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "couponUseDate" -> {
                if (coupon.getCouponUseDate() != null) {
                    cell.setCellValue(coupon.getCouponUseDate());
                    cell.setCellStyle(dateStyle);
                }
            }
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