package intbyte4.learnsmate.campaign_template.service;

import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.mapper.CampaignTemplateMapper;
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
public class CampaignTemplateExcelService {

    private final CampaignTemplateService campaignTemplateService;
    private final CampaignTemplateMapper campaignTemplateMapper;

    private static final Map<String, String> COLUMNS = new LinkedHashMap<>() {{
        put("campaignTemplateCode", "캠페인 번호");
        put("campaignTemplateTitle", "캠페인 제목");
        put("campaignTemplateContents", "캠페인 내용");
        put("createdAt", "생성일");
        put("updatedAt", "수정일");
        put("adminName", "담당자");
    }};

    public void exportCampaignTemplateToExcel(OutputStream outputStream, CampaignTemplateFilterDTO filterDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("CampaignTemplate Data");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            List<String> selectedColumns = filterDTO != null && filterDTO.getSelectedColumns() != null
                    ? filterDTO.getSelectedColumns()
                    : new ArrayList<>(COLUMNS.keySet());

            createHeader(sheet, headerStyle, selectedColumns);

            List<ResponseFindCampaignTemplateByFilterVO> campaignTemplateList;
            if (filterDTO != null) {
                campaignTemplateList = campaignTemplateMapper.fromFindAllDtoListToFindTemplateByFilterVO
                        (campaignTemplateService.findTemplateListByFilterWithExcel(filterDTO));
            } else {
                campaignTemplateList = campaignTemplateMapper.fromFindAllDtoListToFindTemplateByFilterVO
                        (campaignTemplateService.findAllTemplateListWithExcel());
            }
            log.info("Found {} CampaignTemplates to export", campaignTemplateList.size());

            writeData(sheet, campaignTemplateList, dateStyle, selectedColumns);

            for (int i = 0; i < selectedColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Failed to create CampaignTemplate Excel file", e);
            throw new RuntimeException("Failed to create CampaignTemplate Excel file", e);
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

    private void writeData(Sheet sheet, List<ResponseFindCampaignTemplateByFilterVO> campaignTemplateList, CellStyle dateStyle, List<String> selectedColumns) {
        int rowNum = 1;
        for (ResponseFindCampaignTemplateByFilterVO campaignTemplate : campaignTemplateList) {
            Row row = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String key : selectedColumns) {
                Cell cell = row.createCell(columnIndex++);
                setValueByColumnKey(cell, key, campaignTemplate, dateStyle);
            }
        }
    }

    private void setValueByColumnKey(Cell cell, String key, ResponseFindCampaignTemplateByFilterVO campaignTemplate, CellStyle dateStyle) {
        switch (key) {
            case "campaignTemplateCode" -> cell.setCellValue(campaignTemplate.getCampaignTemplateCode());
            case "campaignTemplateTitle" -> cell.setCellValue(campaignTemplate.getCampaignTemplateTitle());
            case "campaignTemplateContents" -> cell.setCellValue(campaignTemplate.getCampaignTemplateContents());
            case "createdAt" -> {
                if (campaignTemplate.getCreatedAt() != null) {
                    // LocalDateTime -> java.util.Date 변환
                    Date date = Date.from(campaignTemplate.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellValue("");
                }
            }
            case "updatedAt" -> {
                if (campaignTemplate.getUpdatedAt() != null) {
                    Date date = Date.from(campaignTemplate.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant());
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellValue("");
                }
            }
            case "adminName" -> cell.setCellValue(campaignTemplate.getAdminName() != null ?
                    campaignTemplate.getAdminName() : "-");
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

