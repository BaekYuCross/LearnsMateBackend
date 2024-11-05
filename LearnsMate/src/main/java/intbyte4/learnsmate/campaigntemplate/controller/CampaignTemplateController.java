package intbyte4.learnsmate.campaigntemplate.controller;

import intbyte4.learnsmate.campaigntemplate.domain.dto.CampaignTemplateDTO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.request.RequestRegisterTemplateVO;
import intbyte4.learnsmate.campaigntemplate.domain.vo.response.ResponseRegisterTemplateVO;
import intbyte4.learnsmate.campaigntemplate.service.CampaignTemplateService;
import intbyte4.learnsmate.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("campaignTemplateController")
@RequestMapping("campaigntemplate")
@Slf4j
public class CampaignTemplateController {

    private final CampaignTemplateService campaignTemplateService;

    @Autowired
    public CampaignTemplateController(final CampaignTemplateService campaignTemplateService) {
        this.campaignTemplateService = campaignTemplateService;
    }

    @Operation(summary = "직원 - 캠페인 템플릿 등록")
    @PostMapping("/register")
    public ResponseEntity<?> registerTemplate(@RequestBody final RequestRegisterTemplateVO request) {
        log.info("템플릿 등록 요청 : {}", request);
        try {
            CampaignTemplateDTO campaignTemplateDTO = new CampaignTemplateDTO();

            CampaignTemplateDTO registerCampaignTemplate = campaignTemplateService.registerTemplate(campaignTemplateDTO);

            ResponseRegisterTemplateVO response = new ResponseRegisterTemplateVO(
                    registerCampaignTemplate.getCampaignTemplateCode(),
                    registerCampaignTemplate.getCampaignTemplateTitle(),
                    registerCampaignTemplate.getCampaignTemplateContents(),
                    registerCampaignTemplate.getCampaignTemplateFlag(),
                    registerCampaignTemplate.getAdminCode()
            );

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
}
