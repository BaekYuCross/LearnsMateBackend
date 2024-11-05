package intbyte4.learnsmate.campaigntemplate.controller;

import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.request.RequestEditTemplateVO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.response.ResponseEditTemplateVO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.response.ResponseRegisterTemplateVO;
import intbyte4.learnsmate.campaigntemplate.mapper.CampaignTemplateMapper;
import intbyte4.learnsmate.campaigntemplate.service.CampaignTemplateService;
import intbyte4.learnsmate.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("campaignTemplateController")
@RequestMapping("campaigntemplate")
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

    @PatchMapping("/{campaignTemplateCode}")
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
}
