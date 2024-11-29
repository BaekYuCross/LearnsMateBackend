package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final CouponService couponService;
    private final CouponFacade couponFacade;

    public void exportCouponToExcel(ServletOutputStream outputStream, CouponFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Coupon Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);


            // 기본적으로 모든 컬럼 사용
            List<String> selectedColumns = new ArrayList<>(COLUMNS.keySet());

            // 필터DTO의 selectedColumns가 있다면 그것을 사용
            if (filterDTO != null && filterDTO.getSelectedColumns() != null
                    && !filterDTO.getSelectedColumns().isEmpty()) {
                selectedColumns = filterDTO.getSelectedColumns();
            }

            createHeader(sheet, headerStyle, selectedColumns);

            List<CouponFindResponseVO> couponList;
            if (filterDTO != null) {
                log.info("Applying filter with DTO: {}", filterDTO);
                couponList = couponFacade.filterCoupon(filterDTO);
                log.info("Found {} coupons after filtering", couponList.size());
            } else {
                log.info("No filter applied, getting all Lectures");
                couponList = couponFacade.findAllCoupons();
                log.info("Found {} total coupons", couponList.size());
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

    // 타겟 쿠폰 업로드
    public List<CouponFindResponseVO> importTargetStudentFromExcel(MultipartFile file) throws IOException {
        List<CouponFindResponseVO> validCouponList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (!validateHeaders(headerRow)) {
                throw new IllegalArgumentException("파일 형식이 올바르지 않습니다. 다운로드한 템플릿 파일만 업로드하세요.");
            }

            int lastRowNum = findLastRow(sheet);
            if (lastRowNum < 1) return validCouponList;

            for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                Long couponCode = getLongValue(row.getCell(0));
                if (couponCode == null) {
                    throw new CommonException(StatusEnum.COUPON_NOT_FOUND);
                }

                CouponEntity existingCoupon = couponService.findByCouponCode(couponCode);
                if (!validateCouponData(existingCoupon, row)) {
                    throw new CommonException(StatusEnum.INVALID_COUPON_DATA);
                }

                CouponFindResponseVO vo = CouponFindResponseVO.builder()
                            .couponCode(existingCoupon.getCouponCode())
                            .couponName(existingCoupon.getCouponName())
                            .couponContents(existingCoupon.getCouponContents())
                            .couponDiscountRate(existingCoupon.getCouponDiscountRate())
                            .createdAt(existingCoupon.getCreatedAt())
                            .updatedAt(existingCoupon.getUpdatedAt())
                            .couponStartDate(existingCoupon.getCouponStartDate())
                            .couponExpireDate(existingCoupon.getCouponExpireDate())
                            .activeState(existingCoupon.getActiveState())
                            .couponCategoryName(existingCoupon.getCouponCategory().getCouponCategoryName())
                            .adminName(existingCoupon.getAdmin().getAdminName())
                            .tutorName(existingCoupon.getTutor().getMemberName())
                            .build();

                validCouponList.add(vo);
            }
        } catch (Exception e) {
            log.error("엑셀 파일 처리 중 오류 발생", e);
            throw new RuntimeException("엑셀 파일 처리 실패", e);
        }

        return validCouponList;
    }

    private Boolean validateCouponData(CouponEntity existingCoupon, Row row) {
        if (!existingCoupon.getCouponCode().equals(getLongValue(row.getCell(0))) ||
                !existingCoupon.getCouponName().equals(getStringValue(row.getCell(1))) ||
                !existingCoupon.getCouponContents().equals(getStringValue(row.getCell(2))) ||
                !existingCoupon.getCouponDiscountRate().equals(getIntValue(row.getCell(3))) ||
                existingCoupon.getCouponCategory() == null ||
                !existingCoupon.getCouponCategory().getCouponCategoryName().equals(getStringValue(row.getCell(4))) ||
                !existingCoupon.getActiveState().equals(getActiveStateFromString(getStringValue(row.getCell(5)))) ||
                !isSameDateTime(existingCoupon.getCouponStartDate(), getLocalDateTime(row.getCell(6))) ||
                !isSameDateTime(existingCoupon.getCouponExpireDate(), getLocalDateTime(row.getCell(7))) ||
                !isSameDateTime(existingCoupon.getCreatedAt(), getLocalDateTime(row.getCell(8))) ||
                !isSameDateTime(existingCoupon.getUpdatedAt(), getLocalDateTime(row.getCell(9))) ||
                existingCoupon.getAdmin() == null ||
                !existingCoupon.getAdmin().getAdminName().equals(getStringValue(row.getCell(10))) ||
                existingCoupon.getTutor() == null ||
                !existingCoupon.getTutor().getMemberName().equals(getStringValue(row.getCell(11))) ||
                existingCoupon.getCouponFlag() == null ||
                !existingCoupon.getCouponFlag()) {

            return false;
        }
        return true;
    }


    // 올바른 헤더인지 판별
    private boolean validateHeaders(Row headerRow) {
        if (headerRow == null) return false;

        int columnIndex = 0;
        for (String expectedHeader : COLUMNS.values()) {
            Cell cell = headerRow.getCell(columnIndex++);
            if (cell == null || !expectedHeader.equals(cell.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    // 데이터의 마지막 행 찾는 메서드
    private int findLastRow(Sheet sheet) {
        int lastRowNum = -1;
        for (Row row : sheet) {
            boolean hasData = false;
            for (Cell cell : row) {
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    hasData = true;
                    break;
                }
            }
            if (hasData) {
                lastRowNum = row.getRowNum();
            }
        }
        return lastRowNum;
    }

    // LocalDateTime 비교 헬퍼 메서드
    private boolean isSameDateTime(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null && date2 == null) return true;
        if (date1 == null || date2 == null) return false;
        return date1.equals(date2);
    }
    private Long getLongValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        return switch (cell.getCellType()) {
            case NUMERIC -> (long) cell.getNumericCellValue(); // Double -> Long 변환
            case STRING -> {
                try {
                    yield Long.parseLong(cell.getStringCellValue().trim()); // 문자열에서 Long 변환
                } catch (NumberFormatException e) {
                    yield null; // 변환 실패 시 null 반환
                }
            }
            default -> null; // 다른 타입은 null 처리
        };
    }


    // 셀 값 읽는 메서드
    private String getStringValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toString();
                }
                yield String.valueOf((long) cell.getNumericCellValue());
            }
            default -> null;
        };
    }

    private int getIntValue(Cell cell) {
        if (cell == null) return 0;

        return switch (cell.getCellType()) {
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING -> {
                try {
                    yield Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    yield 0;
                }
            }
            default -> 0;
        };
    }

    private LocalDateTime getLocalDateTime(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue();
                    }
                    return null;
                case STRING:
                    String dateStr = cell.getStringCellValue();
                    if (dateStr == null || dateStr.trim().isEmpty()) {
                        return null;
                    }
                    // "yyyy-MM-dd'T'HH:mm:ss" 형식으로 파싱
                    return LocalDateTime.parse(dateStr);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("날짜 변환 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }

    private Boolean getBooleanValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                String value = cell.getStringCellValue().trim().toLowerCase();
                if (value.equals("활성") || value.equals("비휴면")) return true;
                if (value.equals("비활성") || value.equals("휴면")) return false;
                return null;
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                return numericValue == 1;
            default:
                return null;
        }
    }

    // 상태값 문자열을 boolean으로 변환
    private Boolean getActiveStateFromString(String state) {
        if (state == null) return false;
        return "활성".equals(state.trim()) || "active".equalsIgnoreCase(state.trim());
    }
}
