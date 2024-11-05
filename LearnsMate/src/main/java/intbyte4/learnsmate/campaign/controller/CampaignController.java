package intbyte4.learnsmate.campaign.controller;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestRegisterCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseRegisterCampaignVO;
import intbyte4.learnsmate.campaign.service.CampaignService;
import intbyte4.learnsmate.common.mapper.CampaignMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("campaignController")
@RequestMapping("campaign")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignMapper campaignMapper;

    @Operation(summary = "직원 - 캠페인 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterCampaignVO> createCampaign(@RequestBody RequestRegisterCampaignVO request) {
        CampaignDTO campaignDTO = campaignService.registerCampaign(campaignMapper.fromRegisterRequestVOtoDTO(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignMapper.fromDtoToRegisterResponseVO(campaignDTO));
    }

    @Operation(summary = "직원 - 예약된 캠페인 수정")
    @PutMapping("/{campaignCode}")
    public ResponseEntity<ResponseEditCampaignVO> updateCampaign(@RequestBody RequestEditCampaignVO request
            , @PathVariable("campaignCode") Long campaignCode) {
        CampaignDTO campaignDTO = campaignService.updateCampaign(campaignMapper.fromEditRequestVOtoDTO(request)
                , campaignCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignMapper.fromDtoToEditResponseVO(campaignDTO));
    }
}
