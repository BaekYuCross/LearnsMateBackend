package intbyte4.learnsmate.campaign_template.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import intbyte4.learnsmate.campaign_template.domain.dto.FindAllCampaignTemplatesDTO;
import intbyte4.learnsmate.campaign_template.domain.dto.FindCampaignTemplateDTO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseRegisterTemplateVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CampaignTemplateMapper {

    public CampaignTemplateDTO fromEntityToDTO(CampaignTemplate campaignTemplate) {
        return CampaignTemplateDTO.builder()
                .campaignTemplateCode(campaignTemplate.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplate.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplate.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplate.getCampaignTemplateFlag())
                .createdAt(campaignTemplate.getCreatedAt())
                .updatedAt(campaignTemplate.getUpdatedAt())
                .adminCode(campaignTemplate.getAdmin().getAdminCode())
                .build();
    }

    public CampaignTemplateDTO fromRegisterRequestVOToDto(RequestRegisterTemplateVO requestRegisterTemplateVO) {
        return CampaignTemplateDTO.builder()
                .campaignTemplateTitle(requestRegisterTemplateVO.getCampaignTemplateTitle())
                .campaignTemplateContents(requestRegisterTemplateVO.getCampaignTemplateContents())
                .adminCode(requestRegisterTemplateVO.getAdminCode())
                .build();
    }

    public CampaignTemplateDTO fromEditRequestVOToDto(RequestEditTemplateVO requestEditTemplateVO) {
        return CampaignTemplateDTO.builder()
                .campaignTemplateTitle(requestEditTemplateVO.getCampaignTemplateTitle())
                .campaignTemplateContents(requestEditTemplateVO.getCampaignTemplateContents())
                .build();
    }

    public ResponseRegisterTemplateVO fromDtoToRegisterResponseVO(CampaignTemplateDTO campaignTemplateDTO) {
        return ResponseRegisterTemplateVO.builder()
                .campaignTemplateCode(campaignTemplateDTO.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplateDTO.getCampaignTemplateFlag())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .adminCode(campaignTemplateDTO.getAdminCode())
                .build();
    }

    public ResponseEditTemplateVO fromDtoToEditResponseVO(CampaignTemplateDTO campaignTemplateDTO) {
        return ResponseEditTemplateVO.builder()
                .campaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents())
                .build();
    }

    public ResponseFindTemplateVO fromDtoToFindResponseVO(CampaignTemplateDTO campaignTemplateDTO) {
        return ResponseFindTemplateVO.builder()
                .campaignTemplateCode(campaignTemplateDTO.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplateDTO.getCampaignTemplateFlag())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .adminCode(campaignTemplateDTO.getAdminCode())
                .build();
    }

    public CampaignTemplate toEntity(CampaignTemplateDTO campaignTemplateDTO, Admin admin) {
        return CampaignTemplate.builder()
                .campaignTemplateCode(campaignTemplateDTO.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplateDTO.getCampaignTemplateFlag())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .admin(admin)
                .build();
    }

    public FindAllCampaignTemplatesDTO fromEntityToFindAllDTO(CampaignTemplate entity, String adminName) {
        return FindAllCampaignTemplatesDTO.builder()
                .campaignTemplateCode(entity.getCampaignTemplateCode())
                .campaignTemplateTitle(entity.getCampaignTemplateTitle())
                .campaignTemplateContents(entity.getCampaignTemplateContents())
                .campaignTemplateFlag(entity.getCampaignTemplateFlag())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .adminCode(entity.getAdmin().getAdminCode())
                .adminName(adminName)
                .build();
    }

    public ResponseFindTemplateVO fromFindAllDtoToFindResponseVO(FindAllCampaignTemplatesDTO dto) {
        return ResponseFindTemplateVO.builder()
                .campaignTemplateCode(dto.getCampaignTemplateCode())
                .campaignTemplateTitle(dto.getCampaignTemplateTitle())
                .campaignTemplateContents(dto.getCampaignTemplateContents())
                .campaignTemplateFlag(dto.getCampaignTemplateFlag())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .adminName(dto.getAdminName())
                .build();
    }

    public ResponseFindCampaignTemplateByFilterVO fromCampaignTemplateToResponseFindCampaignTemplateByFilterVO(CampaignTemplate campaignTemplate) {
        return ResponseFindCampaignTemplateByFilterVO.builder()
                .campaignTemplateCode(campaignTemplate.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplate.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplate.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplate.getCampaignTemplateFlag())
                .createdAt(campaignTemplate.getCreatedAt())
                .updatedAt(campaignTemplate.getUpdatedAt())
                .adminCode(campaignTemplate.getAdmin().getAdminCode())
                .adminName(campaignTemplate.getAdmin().getAdminName())
                .build();
    }

    public CampaignTemplateFilterDTO fromFindCampaignTemplateByFilterVOtoFilterDTO(RequestFindCampaignTemplateByFilterVO vo) {
        return CampaignTemplateFilterDTO.builder()
                .campaignTemplateTitle(vo.getCampaignTemplateTitle())
                .campaignTemplateStartPostDate(vo.getCampaignTemplateStartPostDate())
                .campaignTemplateEndPostDate(vo.getCampaignTemplateEndPostDate())
                .build();
    }

    public FindCampaignTemplateDTO fromEntityToFindCampaignTemplateDTO(CampaignTemplate campaignTemplate) {
        return FindCampaignTemplateDTO.builder()
                .campaignTemplateCode(campaignTemplate.getCampaignTemplateCode())
                .campaignTemplateTitle(campaignTemplate.getCampaignTemplateTitle())
                .campaignTemplateContents(campaignTemplate.getCampaignTemplateContents())
                .campaignTemplateFlag(campaignTemplate.getCampaignTemplateFlag())
                .createdAt(campaignTemplate.getCreatedAt())
                .updatedAt(campaignTemplate.getUpdatedAt())
                .adminCode(campaignTemplate.getAdmin().getAdminCode())
                .adminName(campaignTemplate.getAdmin().getAdminName())
                .build();
    }

    public ResponseFindTemplateVO fromFindCampaignTemplateDtoToFindResponseVO(FindCampaignTemplateDTO findCampaignTemplateDTO) {
        return ResponseFindTemplateVO.builder()
                .campaignTemplateCode(findCampaignTemplateDTO.getCampaignTemplateCode())
                .campaignTemplateTitle(findCampaignTemplateDTO.getCampaignTemplateTitle())
                .campaignTemplateContents(findCampaignTemplateDTO.getCampaignTemplateContents())
                .campaignTemplateFlag(findCampaignTemplateDTO.getCampaignTemplateFlag())
                .createdAt(findCampaignTemplateDTO.getCreatedAt())
                .updatedAt(findCampaignTemplateDTO.getUpdatedAt())
                .adminCode(findCampaignTemplateDTO.getAdminCode())
                .adminName(findCampaignTemplateDTO.getAdminName())
                .build();
    }
}