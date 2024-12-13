package intbyte4.learnsmate.campaign_template.service;

import intbyte4.learnsmate.campaign_template.domain.dto.*;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindCampaignTemplateByFilterVO;

import java.util.List;

public interface CampaignTemplateService {
    CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO);

    CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO);

    void deleteTemplate(CampaignTemplateDTO campaignTemplateDTO);

    List<FindAllCampaignTemplatesDTO> findAllByTemplate();
    // 필터링x 정렬o
    List<FindAllCampaignTemplatesDTO> findAllByTemplateWithSort(String sortField, String sortDirection);

    FindCampaignTemplateDTO findByTemplateCode(Long campaignTemplateCode);

    CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> findCampaignTemplateListByFilter
            (CampaignTemplateFilterDTO request, int page, int size);
    // 필터링o 정렬o
    CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> findCampaignTemplateListByFilterWithSort
            (CampaignTemplateFilterDTO dto, int page, int size, String sortField, String sortDirection);

    List<FindAllCampaignTemplatesDTO> findTemplateListByFilterWithExcel(CampaignTemplateFilterDTO filterDTO);

    List<FindAllCampaignTemplatesDTO> findAllTemplateListWithExcel();
}
