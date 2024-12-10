package intbyte4.learnsmate.campaign_template.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.*;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.mapper.CampaignTemplateMapper;
import intbyte4.learnsmate.campaign_template.repository.CampaignTemplateRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("campaignTemplateService")
@RequiredArgsConstructor
public class CampaignTemplateServiceImpl implements CampaignTemplateService {

    private final CampaignTemplateRepository campaignTemplateRepository;
    private final CampaignTemplateMapper campaignTemplateMapper;
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Override
    @Transactional
    public CampaignTemplateDTO registerTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 등록 중: {}", campaignTemplateDTO);
        AdminDTO adminDTO = getAdminDTO(campaignTemplateDTO);
        log.info(adminDTO.toString());

        CampaignTemplate campaignTemplate = convertToCampaignTemplate(campaignTemplateDTO, adminDTO);
        log.info("데이터베이스에 템플릿 저장 중: {}", campaignTemplate);
        CampaignTemplate savedCampaignTemplate = campaignTemplateRepository.save(campaignTemplate);
        log.info("저장된 템플릿 객체: {}", savedCampaignTemplate);

        return campaignTemplateMapper.fromEntityToDTO(savedCampaignTemplate);
    }

    @Override
    @Transactional
    public CampaignTemplateDTO editTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        log.info("템플릿 수정 중: {}", campaignTemplateDTO);
        CampaignTemplate campaignTemplate = updateCampaignTemplate(campaignTemplateDTO);

        log.info("데이터베이스에 수정된 템플릿 저장 중: {}", campaignTemplate);
        CampaignTemplate updatedCampaignTemplate = campaignTemplateRepository.save(campaignTemplate);
        log.info("수정된 템플릿 객체: {}", updatedCampaignTemplate);

        return campaignTemplateMapper.fromEntityToDTO(updatedCampaignTemplate);
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
    public List<FindAllCampaignTemplatesDTO> findAllByTemplate() {
        log.info("템플릿 전체 조회 중");
        List<CampaignTemplate> campaignTemplateList = campaignTemplateRepository.findAllByCampaignTemplateFlag(Sort.by(Sort.Direction.DESC, "createdAt"),true);
        List<FindAllCampaignTemplatesDTO> findAllCampaignTemplatesDTOList = new ArrayList<>();

        for (CampaignTemplate campaignTemplate : campaignTemplateList) {
            CampaignTemplateDTO campaignTemplateDTO = campaignTemplateMapper.fromEntityToDTO(campaignTemplate);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignTemplateDTO.getAdminCode());
            findAllCampaignTemplatesDTOList.add(campaignTemplateMapper.fromEntityToFindAllDTO(campaignTemplate, adminDTO.getAdminName()));
        }

        return findAllCampaignTemplatesDTOList;
    }

    @Override
    public List<FindAllCampaignTemplatesDTO> findAllByTemplateWithSort(String sortField, String sortDirection) {
        log.info("템플릿 전체 정렬 조회 중 - 필드: {}, 방향: {}", sortField, sortDirection);

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        validateSortField(sortField);

        List<FindAllCampaignTemplatesDTO> findAllCampaignTemplatesDTOList = new ArrayList<>();

        List<CampaignTemplate> campaignTemplateList;
        if (sortField.equals("adminName")) {
            campaignTemplateList = campaignTemplateRepository
                    .findAllByCampaignTemplateFlag(Sort.by(direction, "admin.adminName"), true);
        } else {
            campaignTemplateList = campaignTemplateRepository
                    .findAllByCampaignTemplateFlag(Sort.by(direction, sortField), true);
        }

        for (CampaignTemplate campaignTemplate : campaignTemplateList) {
            CampaignTemplateDTO campaignTemplateDTO = campaignTemplateMapper.fromEntityToDTO(campaignTemplate);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignTemplateDTO.getAdminCode());
            findAllCampaignTemplatesDTOList.add(
                    campaignTemplateMapper.fromEntityToFindAllDTO(campaignTemplate, adminDTO.getAdminName())
            );
        }

        return findAllCampaignTemplatesDTOList;
    }

    private void validateSortField(String sortField) {
        List<String> validFields = Arrays.asList(
                "campaignTemplateCode",
                "campaignTemplateTitle",
                "campaignTemplateContents",
                "adminName",
                "createdAt",
                "updatedAt"
        );

        if (!validFields.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
    }

    @Override
    public FindCampaignTemplateDTO findByTemplateCode(Long campaignTemplateCode) {
        log.info("템플릿 단 건 조회 중: {}", campaignTemplateCode);
        CampaignTemplate campaignTemplate = campaignTemplateRepository.findById(campaignTemplateCode)
                .orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        return campaignTemplateMapper.fromEntityToFindCampaignTemplateDTO(campaignTemplate);
    }

    private CampaignTemplate convertToCampaignTemplate(CampaignTemplateDTO campaignTemplateDTO, AdminDTO adminDTO) {
        Admin user = adminMapper.toEntity(adminDTO);
        campaignTemplateDTO.setCampaignTemplateCode(null);
        campaignTemplateDTO.setCampaignTemplateFlag(true);
        campaignTemplateDTO.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        campaignTemplateDTO.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        campaignTemplateDTO.setAdminCode(adminDTO.getAdminCode());
        return campaignTemplateMapper.toEntity(campaignTemplateDTO, user);
    }

    private AdminDTO getAdminDTO(CampaignTemplateDTO campaignTemplateDTO) {
        campaignTemplateDTO.setAdminCode(campaignTemplateDTO.getAdminCode());

        AdminDTO adminDTO = adminService.findByAdminCode(campaignTemplateDTO.getAdminCode());
        if (adminDTO == null) {
            log.warn("존재하지 않는 직원 : {}", campaignTemplateDTO.getAdminCode());
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        return adminDTO;
    }

    private CampaignTemplate updateCampaignTemplate(CampaignTemplateDTO campaignTemplateDTO) {
        CampaignTemplate campaignTemplate = campaignTemplateRepository.findById(campaignTemplateDTO.getCampaignTemplateCode()).orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        campaignTemplate.setCampaignTemplateTitle(campaignTemplateDTO.getCampaignTemplateTitle());
        campaignTemplate.setCampaignTemplateContents(campaignTemplateDTO.getCampaignTemplateContents());
        campaignTemplate.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        return campaignTemplate;
    }

    @Override
    public CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> findCampaignTemplateListByFilter
            (CampaignTemplateFilterDTO request, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<CampaignTemplate> CampaignTemplatePage = campaignTemplateRepository.searchBy(request, pageable);

        List<ResponseFindCampaignTemplateByFilterVO> campaignTemplateDTOList = CampaignTemplatePage.getContent().stream()
                .map(campaignTemplateMapper::fromCampaignTemplateToResponseFindCampaignTemplateByFilterVO)
                .collect(Collectors.toList());

        return new CampaignTemplatePageResponse<>(
                campaignTemplateDTOList,               // 데이터 리스트
                CampaignTemplatePage.getTotalElements(), // 전체 데이터 수
                CampaignTemplatePage.getTotalPages(),    // 전체 페이지 수
                CampaignTemplatePage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                CampaignTemplatePage.getSize()           // 페이지 크기
        );
    }

    // 필터링o 정렬o
    @Override
    public CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> findCampaignTemplateListByFilterWithSort(
            CampaignTemplateFilterDTO dto, int page, int size, String sortField, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CampaignTemplate> CampaignTemplatePage = campaignTemplateRepository.searchByWithSort(dto, pageable);

        List<ResponseFindCampaignTemplateByFilterVO> campaignTemplateDTOList = CampaignTemplatePage.getContent().stream()
                .map(campaignTemplateMapper::fromCampaignTemplateToResponseFindCampaignTemplateByFilterVO)
                .collect(Collectors.toList());

        return new CampaignTemplatePageResponse<>(
                campaignTemplateDTOList,
                CampaignTemplatePage.getTotalElements(),
                CampaignTemplatePage.getTotalPages(),
                CampaignTemplatePage.getNumber() + 1,
                CampaignTemplatePage.getSize()
        );
    }

    @Override
    public List<FindAllCampaignTemplatesDTO> findTemplateListByFilterWithExcel(CampaignTemplateFilterDTO filterDTO) {
        List<CampaignTemplate> campaignTemplateList = campaignTemplateRepository.searchByWithoutPaging(filterDTO);
        return campaignTemplateList.stream()
                        .map(campaignTemplateMapper::fromTemplateToFindAllCampaignTemplateDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public List<FindAllCampaignTemplatesDTO> findAllTemplateListWithExcel() {
        List<CampaignTemplate> campaignTemplates = campaignTemplateRepository.findAll();

        List<FindAllCampaignTemplatesDTO> findAllCampaignTemplatesDTOList = new ArrayList<>();
        for(CampaignTemplate campaignTemplate : campaignTemplates) {
            CampaignTemplateDTO campaignTemplateDTO = campaignTemplateMapper.fromEntityToDTO(campaignTemplate);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignTemplateDTO.getAdminCode());
            findAllCampaignTemplatesDTOList.add(campaignTemplateMapper.toFindAllTemplateDTO(campaignTemplateDTO, adminDTO.getAdminName()));
        }

        return findAllCampaignTemplatesDTOList;
    }

}
