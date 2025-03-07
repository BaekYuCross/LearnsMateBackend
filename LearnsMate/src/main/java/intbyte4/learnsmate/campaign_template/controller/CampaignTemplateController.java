package intbyte4.learnsmate.campaign_template.controller;

import intbyte4.learnsmate.campaign_template.domain.dto.*;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseEditTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindCampaignTemplateByFilterVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseFindTemplateVO;
import intbyte4.learnsmate.campaign_template.domain.vo.response.ResponseRegisterTemplateVO;
import intbyte4.learnsmate.campaign_template.mapper.CampaignTemplateMapper;
import intbyte4.learnsmate.campaign_template.service.CampaignTemplateService;
import intbyte4.learnsmate.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("campaignTemplateController")
@RequestMapping("campaign-template")
@Slf4j
@RequiredArgsConstructor
public class CampaignTemplateController {

    private final CampaignTemplateService campaignTemplateService;
    private final CampaignTemplateMapper campaignTemplateMapper;

    @Operation(summary = "직원 - 캠페인 템플릿 등록")
    @PostMapping("/register")
    public ResponseEntity<?> registerTemplate(@RequestBody final RequestRegisterTemplateVO request) {
        log.info("템플릿 등록 요청 : {}", request);
        try {
            CampaignTemplateDTO campaignTemplateDTO = campaignTemplateMapper.fromRegisterRequestVOToDto(request);

            CampaignTemplateDTO registerCampaignTemplate = campaignTemplateService.registerTemplate(campaignTemplateDTO);

            ResponseRegisterTemplateVO response = campaignTemplateMapper.fromDtoToRegisterResponseVO(registerCampaignTemplate);

            log.info("캠페인 템플릿 등록 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("캠페인 템플릿 등록 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }

    @Operation(summary = "직원 - 캠페인 템플릿 수정")
    @PatchMapping("/edit/{campaignTemplateCode}")
    public ResponseEntity<?> editTemplate(@PathVariable("campaignTemplateCode") Long campaignTemplateCode, @RequestBody RequestEditTemplateVO request) {
        log.info("템플릿 수정 요청 : {}", request);
        try {
            CampaignTemplateDTO campaignTemplateDTO = campaignTemplateMapper.fromEditRequestVOToDto(request);
            campaignTemplateDTO.setCampaignTemplateCode(campaignTemplateCode);

            CampaignTemplateDTO updatedTemplateDTO = campaignTemplateService.editTemplate(campaignTemplateDTO);

            ResponseEditTemplateVO response = campaignTemplateMapper.fromDtoToEditResponseVO(updatedTemplateDTO);

            log.info("캠페인 템플릿 수정 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("캠페인 템플릿 수정 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }

    @Operation(summary = "직원 - 캠페인 템플릿 삭제처리")
    @PatchMapping("/delete/{campaignTemplateCode}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("campaignTemplateCode") Long campaignTemplateCode) {
        log.info("템플릿 삭제 요청된 템플릿 코드 : {}", campaignTemplateCode);
        try {
            CampaignTemplateDTO campaignTemplateDTO = new CampaignTemplateDTO();
            campaignTemplateDTO.setCampaignTemplateCode(campaignTemplateCode);

            campaignTemplateService.deleteTemplate(campaignTemplateDTO);

            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 삭제되었습니다.");
        } catch (CommonException e) {
            log.error("캠페인 템플릿 삭제 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }

    @Operation(summary = "직원 - 캠페인 템플릿 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseFindTemplateVO>> listTemplates() {
        List<FindAllCampaignTemplatesDTO> campaignTemplateDTOList = campaignTemplateService.findAllByTemplate();
        List<ResponseFindTemplateVO> response = new ArrayList<>();
        for (FindAllCampaignTemplatesDTO findAllCampaignTemplateDTO : campaignTemplateDTOList) {
            ResponseFindTemplateVO vocVO = campaignTemplateMapper.fromFindAllDtoToFindResponseVO(findAllCampaignTemplateDTO);
            response.add(vocVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 필터링x 정렬o
    @Operation(summary = "직원 - 캠페인 템플릿 전체 조회 + 컬럼 정렬")
    @GetMapping("/list/sort")
    public ResponseEntity<List<ResponseFindTemplateVO>> listTemplatesWithSort(
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection) {
        List<FindAllCampaignTemplatesDTO> campaignTemplateDTOList = campaignTemplateService.findAllByTemplateWithSort(sortField, sortDirection);
        List<ResponseFindTemplateVO> response = new ArrayList<>();
        for (FindAllCampaignTemplatesDTO findAllCampaignTemplateDTO : campaignTemplateDTOList) {
            ResponseFindTemplateVO vocVO = campaignTemplateMapper.fromFindAllDtoToFindResponseVO(findAllCampaignTemplateDTO);
            response.add(vocVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "직원 - 캠페인 템플릿 단 건 조회")
    @GetMapping("/{campaignTemplateCode}")
    public ResponseEntity<?> getTemplate(@PathVariable("campaignTemplateCode") Long campaignTemplateCode) {
        log.info("템플릿 조회 요청된 템플릿 코드 : {}", campaignTemplateCode);
        try {
            FindCampaignTemplateDTO findCampaignTemplateDTO = campaignTemplateService.findByTemplateCode(campaignTemplateCode);
            ResponseFindTemplateVO response = campaignTemplateMapper.fromFindCampaignTemplateDtoToFindResponseVO(findCampaignTemplateDTO);
            log.info("캠페인 템플릿 조회 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("캠페인 템플릿 조회 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }

    @Operation(summary = "직원 - 조건 별 캠페인 템플릿 조회")
    @PostMapping("/filter")
    public ResponseEntity<CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO>> filterCampaignTemplates
            (@RequestBody RequestFindCampaignTemplateByFilterVO request,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "15") int size){
        CampaignTemplateFilterDTO dto =
                campaignTemplateMapper.fromFindCampaignTemplateByFilterVOtoFilterDTO(request);
        log.info("반환된 조건 별 캠페인 : {}", dto);
        CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> response = campaignTemplateService
                .findCampaignTemplateListByFilter(dto, page, size);

        return ResponseEntity.ok(response);
    }

    // 필터링o 정렬o
    @Operation(summary = "직원 - 조건 별 캠페인 템플릿 조회")
    @PostMapping("/filter/sort")
    public ResponseEntity<CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO>> filterCampaignTemplatesWithSort
            (@RequestBody RequestFindCampaignTemplateByFilterVO request,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "50") int size,
             @RequestParam(required = false, defaultValue = "createdAt") String sortField,
             @RequestParam(required = false, defaultValue = "DESC") String sortDirection) {
        CampaignTemplateFilterDTO dto =
                campaignTemplateMapper.fromFindCampaignTemplateByFilterVOtoFilterDTO(request);
        log.info("반환된 조건 별 캠페인 : {}", dto);
        CampaignTemplatePageResponse<ResponseFindCampaignTemplateByFilterVO> response = campaignTemplateService
                .findCampaignTemplateListByFilterWithSort(dto, page, size, sortField, sortDirection);

        return ResponseEntity.ok(response);
    }

}
