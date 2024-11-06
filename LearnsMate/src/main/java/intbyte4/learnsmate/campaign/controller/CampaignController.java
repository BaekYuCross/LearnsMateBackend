package intbyte4.learnsmate.campaign.controller;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignByCampaignCodeVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestRegisterCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseRegisterCampaignVO;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.campaign.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/edit/{campaignCode}")
    public ResponseEntity<ResponseEditCampaignVO> updateCampaign(@RequestBody RequestEditCampaignVO request
            , @PathVariable("campaignCode") Long campaignCode) {
        CampaignDTO campaignDTO = campaignService.editCampaign(campaignMapper.fromEditRequestVOtoDTO(request)
                , campaignCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignMapper.fromDtoToEditResponseVO(campaignDTO));
    }

    @Operation(summary = "직원 - 예약된 캠페인 취소")
    @DeleteMapping("/delete/{campaignCode}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable("campaignCode") Long campaignCode) {
        CampaignDTO getCampaignCode = new CampaignDTO();
        getCampaignCode.setCampaignCode(campaignCode);
        campaignService.removeCampaign(getCampaignCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "직원 - 캠페인 전체 조회")
    @GetMapping("/campaigns")
    public ResponseEntity<List<ResponseFindCampaignVO>> getAllCampaigns() {
        List<CampaignDTO> campaignDTOList = campaignService.findAllCampaigns();
        List<ResponseFindCampaignVO> responseFindCampaignVOList = campaignMapper
                .fromDtoListToFindCampaignVO(campaignDTOList);

        return new ResponseEntity<>(responseFindCampaignVOList, HttpStatus.OK);
    }

    @Operation(summary = "직원 - 캠페인 단건 조회")
    @GetMapping("/campaign")
    public ResponseEntity<ResponseFindCampaignVO> getCampaign
            (@RequestBody RequestFindCampaignByCampaignCodeVO requestFindCampaignByCampaignCodeVO) {
        CampaignDTO getCampaignCode = campaignMapper.fromFindRequestVOtoDTO(requestFindCampaignByCampaignCodeVO);
        CampaignDTO campaignDTO = campaignService.findCampaign(getCampaignCode);

        return ResponseEntity.status(HttpStatus.OK).body(campaignMapper.fromDtoToFindResponseVO(campaignDTO));
    }
}
