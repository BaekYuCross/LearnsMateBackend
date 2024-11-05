package intbyte4.learnsmate.campaigntemplate.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaigntemplate.domain.CampaignTemplate;
import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaigntemplate.mapper.CampaignTemplateMapper;
import intbyte4.learnsmate.campaigntemplate.repository.CampaignTemplateRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("campaignTemplateService")
@RequiredArgsConstructor
public class CampaignTemplateServiceImpl implements CampaignTemplateService {

    private final CampaignTemplateRepository campaignTemplateRepository;
    private final CampaignTemplateMapper campaignTemplateMapper;
    private final AdminService adminService;

    @Override
    @Transactional
    public CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 등록 중: {}", campaignTemplateDTO);
        campaignTemplateDTO.setAdminCode(campaignTemplateDTO.getAdminCode());

        AdminDTO adminDTO = adminService.findByAdminCode(campaignTemplateDTO.getAdminCode());
        if (adminDTO == null) {
            log.warn("존재하지 않는 사용자 : {}", campaignTemplateDTO.getAdminCode());
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        log.info(adminDTO.toString());

        Admin user = adminDTO.convertToEntity();
        CampaignTemplate campaignTemplate = campaignTemplateMapper.toEntity(campaignTemplateDTO, user);

        log.info("데이터베이스에 템플릿 저장 중: {}", campaignTemplate);
        CampaignTemplate savedCampaignTemplate = campaignTemplateRepository.save(campaignTemplate);
        log.info("저장된 템플릿 객체: {}", savedCampaignTemplate);

        return campaignTemplateMapper.fromEntityToDto(savedCampaignTemplate);
    }

}
