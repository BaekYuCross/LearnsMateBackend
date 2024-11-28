package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
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
public class CouponExcelService {

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("couponCode", "쿠폰 번호");
        put("couponName", "쿠폰 이름");
        put("couponContents", "쿠폰 내용");
        put("couponDiscountRate", "쿠폰 할인율");
        put("couponCategoryName", "쿠폰 종류");
        put("activeState", "상태");
        put("couponStartDate", "시작일");
        put("couponExpireDate", "만료일");
        put("createdAt", "생성일");
        put("updatedAt", "수정일");
        put("adminName", "직원");
        put("tutorName", "강사");
    }};
    private final CouponServiceImpl couponService;
    private final CouponFacade couponFacade;

    public void exportCouponToExcel(ServletOutputStream outputStream, CouponFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Coupon Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            List<String> selectedColumns = filterDTO != null && filterDTO.getSelectedColumns() != null
                    ? filterDTO.getSelectedColumns()
                    : new ArrayList<>(COLUMNS.keySet());

            createHeader(sheet, headerStyle, selectedColumns);

            List<CouponFindResponseVO> couponList;
            if (filterDTO != null) {
                log.info("Applying filter with DTO: {}", filterDTO);
                couponList = couponFacade.filterCoupon(filterDTO);
            } else {
                log.info("No filter applied, getting all Lectures");
                couponList = couponFacade.findAllCoupons();
            }
            log.info("Found {} Lectures to export", couponList.size());

            writeData(sheet, couponList, dateStyle, selectedColumns);

            for (int i = 0; i < selectedColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Lecture Excel file", e);
            throw new RuntimeException("Failed to create Lecture Excel file", e);
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

    private void writeData(Sheet sheet, List<CouponFindResponseVO> couponList, CellStyle dateStyle, List<String> selectedColumns) {
        int rowNum = 1;
        for (CouponFindResponseVO coupon : couponList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : selectedColumns) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, coupon, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, CouponFindResponseVO coupon, CellStyle dateStyle) {
        switch (key) {
            case "couponCode" -> cell.setCellValue(coupon.getCouponCode());
            case "couponName" -> cell.setCellValue(coupon.getCouponName());
            case "couponContents" -> cell.setCellValue(coupon.getCouponContents());
            case "couponDiscountRate" -> cell.setCellValue(coupon.getCouponDiscountRate() + "%");
            case "couponCategoryName" -> cell.setCellValue(coupon.getCouponCategoryName());
            case "activeState" -> cell.setCellValue(coupon.getActiveState() ? "활성" : "비활성");
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
            case "createdAt" -> {
                if (coupon.getCreatedAt() != null) {
                    cell.setCellValue(coupon.getCreatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "updatedAt" -> {
                if (coupon.getUpdatedAt() != null) {
                    cell.setCellValue(coupon.getUpdatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "adminName" -> cell.setCellValue(coupon.getAdminName() != null ?
                    coupon.getAdminName() : "-");
            case "tutorName" -> cell.setCellValue(coupon.getTutorName() != null ?
                    coupon.getTutorName() : "-");
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
