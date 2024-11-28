package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByFilterVO;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignExcelService {

    private final CampaignService campaignService;
    private final CampaignMapper campaignMapper;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("campaignCode", "캠페인 번호");
        put("campaignTitle", "캠페인 제목");
        put("campaignContents", "캠페인 내용");
        put("campaignType", "발송 타입");
        put("campaignSendDate", "발송 날짜");
        put("createdAt", "생성일");
        put("updatedAt", "수정일");
        put("adminName", "담당자");
    }};

    public void exportCampaignToExcel(OutputStream outputStream, CampaignFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Campaign Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            List<String> selectedColumns = filterDTO != null && filterDTO.getSelectedColumns() != null
                    ? filterDTO.getSelectedColumns()
                    : new ArrayList<>(COLUMNS.keySet());

            createHeader(sheet, headerStyle, selectedColumns);

            List<ResponseFindCampaignByFilterVO> campaignList;
            if (filterDTO != null) {
                campaignList = campaignMapper.fromFindAllDtoListToFindCampaignByFilterVO
                        (campaignService.findCampaignListByConditionWithExcel(filterDTO));
            } else {
                campaignList = campaignMapper.fromFindAllDtoListToFindCampaignByFilterVO
                        (campaignService.findAllCampaignListWithExcel());
            }
            log.info("Found {} Campaigns to export", campaignList.size());

            writeData(sheet, campaignList, dateStyle, selectedColumns);

            for (int i = 0; i < selectedColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create Campaign Excel file", e);
            throw new RuntimeException("Failed to create Campaign Excel file", e);
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

    private void writeData(Sheet sheet, List<ResponseFindCampaignByFilterVO> campaignList, CellStyle dateStyle, List<String> selectedColumns) {
        int rowNum = 1;
        for (ResponseFindCampaignByFilterVO campaign : campaignList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : selectedColumns) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, campaign, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindCampaignByFilterVO campaign, CellStyle dateStyle) {
        switch (key) {
            case "campaignCode" -> cell.setCellValue(campaign.getCampaignCode());
            case "campaignTitle" -> cell.setCellValue(campaign.getCampaignTitle());
            case "campaignContents" -> cell.setCellValue(campaign.getCampaignContents());
            case "campaignType" -> cell.setCellValue(campaign.getCampaignType());
            case "campaignSendDate" -> {
                if (campaign.getCampaignSendDate() != null) {
                    Date date = Date.from(campaign.getCampaignSendDate().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                }
                else {
                    cell.setCellValue("");
                }
            }

            case "createdAt" -> {
                if (campaign.getCreatedAt() != null) {
                    // LocalDateTime -> java.util.Date 변환
                    Date date = Date.from(campaign.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellValue("");
                }
            }
            case "updatedAt" -> {
                if (campaign.getUpdatedAt() != null) {
                    Date date = Date.from(campaign.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellValue("");
                }
            }
            case "adminName" -> cell.setCellValue(campaign.getAdminName() != null ?
                    campaign.getAdminName() : "-");
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
