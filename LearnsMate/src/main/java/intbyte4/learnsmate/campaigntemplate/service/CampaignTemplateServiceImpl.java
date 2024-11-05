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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            log.warn("존재하지 않는 직원 : {}", campaignTemplateDTO.getAdminCode());
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

    @Override
    @Transactional
    public CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 수정 중: {}", campaignTemplateDTO);

        CampaignTemplate campaignTemplate = campaignTemplateRepository.findById(campaignTemplateDTO.getCampaignTemplateCode()).orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        campaignTemplate.setCampaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle());
        campaignTemplate.setCampaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents());
        campaignTemplate.setUpdatedAt(LocalDateTime.now());

        log.info("데이터베이스에 수정된 템플릿 저장 중: {}", campaignTemplate);
        CampaignTemplate updatedCampaignTemplate = campaignTemplateRepository.save(campaignTemplate);
        log.info("수정된 템플릿 객체: {}", updatedCampaignTemplate);

        return campaignTemplateMapper.fromEntityToDto(updatedCampaignTemplate);
    }

    @Override
    @Transactional
    public void deleteTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 삭제 중: {}", campaignTemplateDTO);

        CampaignTemplate campaignTemplate = campaignTemplateRepository.findById(campaignTemplateDTO.getCampaignTemplateCode()).orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        campaignTemplate.setCampaignTemplateFlag(false);

        log.info("데이터베이스에 해당 템플릿 삭제 처리 중: {}", campaignTemplate);
        CampaignTemplate deletedCampaignTemplate = campaignTemplateRepository.save(campaignTemplate);
        log.info("삭제 처리된 템플릿 객체: {}", deletedCampaignTemplate);
    }

    @Override
    public List<CampaignTemplateDTO> findAllByTemplate() {
        log.info("템플릿 전체 조회 중");
        List<CampaignTemplate> campaignTemplateList = campaignTemplateRepository.findAll();
        List<CampaignTemplateDTO> campaignTemplateDTOList = new ArrayList<>();

        for (CampaignTemplate campaignTemplate : campaignTemplateList) {
            campaignTemplateDTOList.add(campaignTemplateMapper.fromEntityToDto(campaignTemplate));
        }

        return campaignTemplateDTOList;
    }

    @Override
    public CampaignTemplateDTO findByTemplateCode(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 단 건 조회 중: {}", campaignTemplateDTO);
        CampaignTemplate campaignTemplate = campaignTemplateRepository.findById(campaignTemplateDTO.getCampaignTemplateCode())
                .orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));

        return campaignTemplateMapper.fromEntityToDto(campaignTemplate);
    }
}
