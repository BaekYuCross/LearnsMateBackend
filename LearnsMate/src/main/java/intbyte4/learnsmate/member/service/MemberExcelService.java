package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberExcelService {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("memberCode", "회원 코드");
        put("memberName", "회원 이름");
        put("memberEmail", "회원 이메일");
        put("memberPhone", "연락처");
        put("memberAddress", "회원 주소");
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
        log.info("write Data(memberList.size()): {}",memberList.size());
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
            case "memberCode" -> cell.setCellValue(member.getMemberCode() != null ? member.getMemberCode() : 0L);
            case "memberName" -> cell.setCellValue(member.getMemberName() != null ? member.getMemberName() : "");
            case "memberEmail" -> cell.setCellValue(member.getMemberEmail() != null ? member.getMemberEmail() : "");
            case "memberPhone" -> cell.setCellValue(member.getMemberPhone() != null ? member.getMemberPhone() : "");
            case "memberAddress" -> cell.setCellValue(member.getMemberAddress() != null ? member.getMemberAddress() : "");
            case "memberAge" -> cell.setCellValue(member.getMemberAge() != null ? member.getMemberAge() : 0);
            case "memberBirth" -> {
                if (member.getMemberBirth() != null) {
                    // LocalDateTime -> java.util.Date 변환
                    Date date = Date.from(member.getMemberBirth().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date); // 날짜를 셀에 입력
                    cell.setCellStyle(dateStyle); // 날짜 스타일 적용
                } else {
                    cell.setCellValue(""); // null일 경우 빈 값 처리
                }
            }
            case "memberFlag" -> cell.setCellValue(member.getMemberFlag() != null && member.getMemberFlag() ? "활성" : "비활성");
            case "createdAt" -> {
                if (member.getCreatedAt() != null) {
                    // LocalDateTime -> java.util.Date 변환
                    Date date = Date.from(member.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date); // 날짜를 셀에 입력
                    cell.setCellStyle(dateStyle); // 날짜 스타일 적용
                } else {
                    cell.setCellValue(""); // null일 경우 빈 값 처리
                }
            }
            case "memberDormantStatus" -> cell.setCellValue(member.getMemberDormantStatus() != null && member.getMemberDormantStatus() ? "휴면" : "활성");
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

    public void importMemberFromExcel(MultipartFile file, MemberType memberType) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // 실제 사용된 행 찾기
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
            log.info("마지막 데이터가 있는 행 번호: {}", lastRowNum);
            if (lastRowNum < 1) return;  // 헤더만 있거나 데이터가 없는 경우

            List<MemberDTO> memberDTOList = new ArrayList<>();
            // 헤더 다음 행부터 마지막 데이터가 있는 행까지만 처리
            for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                log.info("Processing row: {}", rowNum);

//                // 각 셀의 원본 값 출력
//                log.info("=== Row {} 데이터 ===", row.getRowNum());
//                for (int i = 0; i < 11; i++) {  // 0부터 10까지의 cell 확인
//                    Cell cell = row.getCell(i);
//                    if (cell != null) {
//                        log.info("Cell {}: Type={}, Value={}",
//                                i,
//                                cell.getCellType(),
//                                getCellValueAsString(cell)
//                        );
//                    } else {
//                        log.info("Cell {}: NULL", i);
//                    }
//                }
//                log.info("==================");

                MemberDTO memberDTO = MemberDTO.builder()
                        .memberName(getStringValue(row.getCell(1)))
                        .memberEmail(getStringValue(row.getCell(2)))
                        .memberPhone(getStringValue(row.getCell(3)))
                        .memberAddress(getStringValue(row.getCell(4)))
                        .memberAge(getIntValue(row.getCell(5)))
                        .memberBirth(getLocalDateTime(row.getCell(6)))
                        .memberFlag(getBooleanValue(row.getCell(7)))
                        .createdAt(LocalDateTime.now())
                        .memberDormantStatus(getBooleanValue(row.getCell(9)))
                        .memberPassword(getStringValue(row.getCell(10)))
                        .memberType(memberType)
                        .updatedAt(LocalDateTime.now())
                        .build();

                log.info("생성된 DTO: {}", memberDTO);
                memberDTOList.add(memberDTO);
            }

            memberDTOList.forEach(memberDTO -> memberService.saveMember(memberDTO));
        } catch (Exception e) {
            log.error("엑셀 파일 처리 중 오류 발생", e);
            throw new RuntimeException("엑셀 파일 처리 실패", e);
        }
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


    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toString();
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "BLANK";
            default -> "UNSUPPORTED TYPE: " + cell.getCellType();
        };
    }
}