package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberExcelService {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("memberCode", "학생 코드");
        put("memberName", "학생 이름");
        put("memberEmail", "학생 이메일");
        put("memberPhone", "연락처");
        put("memberAddress", "학생 주소");
        put("memberAge", "나이");
        put("memberBirth", "생년월일");
        put("memberFlag", "계정상태");
        put("createdAt", "생성일");
        put("memberDormantStatus", "휴면상태");
    }};

    public void exportMemberToExcel(OutputStream outputStream, MemberFilterRequestDTO filterDTO, MemberType memberType) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet;
            // MemberType에 따라 시트 이름을 설정
            if (memberType.equals(MemberType.STUDENT)) {
                sheet = workbook.createSheet("학생 목록");
            } else if (memberType.equals(MemberType.TUTOR)) {
                sheet = workbook.createSheet("강사 목록");
            } else {
                throw new CommonException(StatusEnum.MISSING_REQUEST_PARAMETER);
            }
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            createHeader(sheet, headerStyle);

            List<ResponseFindMemberVO> memberList;
            if (filterDTO != null) {
                filterDTO.setMemberType(memberType);
                log.info("Applying filter with DTO: {}", filterDTO);
                memberList = memberService.findAllByFilterWithExcel(filterDTO).stream()
                        .map(memberMapper::fromMemberDTOtoResponseFindMemberVO)
                        .collect(Collectors.toList());
            } else {
                log.info("No filter applied, getting all members");
                memberList = memberService.findAllByMemberTypeWithExcel(memberType).stream()
                        .map(memberMapper::fromMemberDTOtoResponseFindMemberVO)
                        .collect(Collectors.toList());
            }
            log.info("Found {} members to export", memberList.size());

            writeData(sheet, memberList, dateStyle);

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

    private void writeData(Sheet sheet, List<ResponseFindMemberVO> memberList, CellStyle dateStyle) {
        int rowNum = 1;
        for (ResponseFindMemberVO member : memberList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : COLUMNS.keySet()) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, member, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindMemberVO member, CellStyle dateStyle) {
        switch (key) {
            case "memberCode" -> cell.setCellValue(member.getMemberCode());
            case "memberName" -> cell.setCellValue(member.getMemberName());
            case "memberEmail" -> cell.setCellValue(member.getMemberEmail());
            case "memberPhone" -> cell.setCellValue(member.getMemberPhone());
            case "memberAddress" -> cell.setCellValue(member.getMemberAddress());
            case "memberAge" -> cell.setCellValue(member.getMemberAge());
            case "memberBirth" -> {
                if (member.getMemberBirth() != null) {
                    cell.setCellValue(member.getMemberBirth());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "memberFlag" -> cell.setCellValue(member.getMemberFlag() ? "활성" : "비활성");
            case "createdAt" -> {
                if (member.getCreatedAt() != null) {
                    cell.setCellValue(member.getCreatedAt());
                    cell.setCellStyle(dateStyle);
                }
            }
            case "memberDormantStatus" -> cell.setCellValue(member.getMemberDormantStatus() ? "휴면" : "활성");
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