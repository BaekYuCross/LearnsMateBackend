package intbyte4.learnsmate.campaign_template.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseRegisterTemplateVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CampaignTemplateMapper {

    public ResponseFindTemplateVO fromEntityToVO(CampaignTemplate campaignTemplate) {
        return ResponseFindTemplateVO.builder()
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
                .campaignTemplateCode(requestRegisterTemplateVO.getCampaignTemplateCode())
                .campaignTemplateTitle(requestRegisterTemplateVO.getCampaignTemplateTitle())
                .campaignTemplateContents(requestRegisterTemplateVO.getCampaignTemplateContents())
                .campaignTemplateFlag(requestRegisterTemplateVO.getCampaignTemplateFlag())
                .createdAt(requestRegisterTemplateVO.getCreatedAt())
                .updatedAt(requestRegisterTemplateVO.getUpdatedAt())
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
                .updatedAt(LocalDateTime.now())
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
}
