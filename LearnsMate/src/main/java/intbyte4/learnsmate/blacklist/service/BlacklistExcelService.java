package intbyte4.learnsmate.blacklist.service;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindBlacklistVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.MemberType;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlacklistExcelService {

    private final BlacklistService blacklistService;
    private final BlacklistMapper blacklistMapper;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("blackCode", "블랙리스트 코드");
        put("memberCode", "회원 코드");
        put("memberName", "회원 이름");
        put("memberEmail", "회원 이메일");
        put("blackReason", "블랙리스트 사유");
        put("createdAt", "정지일");
        put("adminName", "담당자");
    }};

    public void exportBlacklistToExcel(ServletOutputStream outputStream, BlacklistFilterRequestDTO filterDTO, MemberType memberType) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet;
            // MemberType에 따라 시트 이름을 설정
            if (memberType.equals(MemberType.STUDENT)) {
                sheet = workbook.createSheet("학생 블랙리스트 목록");
            } else if (memberType.equals(MemberType.TUTOR)) {
                sheet = workbook.createSheet("강사 블랙리스트 목록");
            } else {
                throw new CommonException(StatusEnum.MISSING_REQUEST_PARAMETER);
            }

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            createHeader(sheet, headerStyle);

            List<ResponseFindBlacklistVO> blacklistList;
            if (filterDTO != null) {
                filterDTO.setMemberType(memberType);
                log.info("Applying filter with DTO: {}", filterDTO);
                blacklistList = blacklistService.findAllByFilterWithExcel(filterDTO).stream()
                        .map(blacklistMapper::fromBlacklistDTOtoResponseFindBlacklistVO)
                        .collect(Collectors.toList());
            } else {
                log.info("No filter applied, getting all members by memberType");
                blacklistList = blacklistService.findAllByMemberTypeWithExcel(memberType).stream()
                        .map(blacklistMapper::fromBlacklistDTOtoResponseFindBlacklistVO)
                        .collect(Collectors.toList());
            }
            log.info("Found {} members to export", blacklistList.size());

            writeData(sheet, blacklistList, dateStyle);

            for (int i = 0; i < COLUMNS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Member Excel file", e);
            throw new RuntimeException("Failed to create Member Excel file", e);
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

    private void writeData(Sheet sheet, List<ResponseFindBlacklistVO> blacklistList, CellStyle dateStyle) {
        int rowNum = 1;
        log.info("write Data(memberList.size()): {}", blacklistList.size());
        for (ResponseFindBlacklistVO blacklist : blacklistList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : COLUMNS.keySet()) {

                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, blacklist, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindBlacklistVO blacklist, CellStyle dateStyle) {
        switch (key) {
            case "blackCode" -> cell.setCellValue(blacklist.getBlackCode() != null ? blacklist.getBlackCode() : 0L);
            case "memberCode" -> cell.setCellValue(blacklist.getMemberCode() != null ? blacklist.getMemberCode() : 0L);
            case "memberName" -> cell.setCellValue(blacklist.getMemberName() != null ? blacklist.getMemberName() : "");
            case "memberEmail" -> cell.setCellValue(blacklist.getMemberEmail() != null ? blacklist.getMemberEmail() : "");
            case "blackReason" -> cell.setCellValue(blacklist.getBlackReason() != null ? blacklist.getBlackReason() : "");
            case "createdAt" -> {
                if (blacklist.getCreatedAt() != null) {
                    Date date = Date.from(blacklist.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellValue("");
                }
            }
            case "adminName" -> cell.setCellValue(blacklist.getAdminName() != null ? blacklist.getAdminName() : "");
            default -> cell.setCellValue("");
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
