package intbyte4.learnsmate.campaigntemplate.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaigntemplate.domain.CampaignTemplate;
import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.response.ResponseRegisterTemplateVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CampaignTemplateMapper {

    public CampaignTemplateDTO fromEntityToDto(CampaignTemplate campaignTemplate) {
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

    public CampaignTemplateDTO fromRequestVOToDto(RequestRegisterTemplateVO requestRegisterTemplateVO) {
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

    public ResponseRegisterTemplateVO fromDtoToResponseVO(CampaignTemplateDTO campaignTemplateDTO) {
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
