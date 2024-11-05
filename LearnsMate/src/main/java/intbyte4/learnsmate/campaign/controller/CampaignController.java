package intbyte4.learnsmate.campaign.controller;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestInsertCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseInsertCampaignVO;
import intbyte4.learnsmate.campaign.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("campaignController")
@RequestMapping("campaign")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;

    @Operation(summary = "직원 - 캠페인 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseInsertCampaignVO> registerCampaign(@RequestBody RequestInsertCampaignVO request) {
        CampaignDTO campaignDTO = campaignService.registerCampaign(request.toDTO());
        ResponseInsertCampaignVO response = new ResponseInsertCampaignVO();
        return ResponseEntity.status(HttpStatus.CREATED).body(response.fromDTO(campaignDTO));
    }

}
