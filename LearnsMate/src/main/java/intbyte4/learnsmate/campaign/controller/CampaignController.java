package intbyte4.learnsmate.campaign.controller;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.dto.FindAllCampaignDTO;
import intbyte4.learnsmate.campaign.domain.vo.request.*;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseRegisterCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByConditionVO;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.campaign.service.CampaignService;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController("campaignController")
@RequestMapping("campaign")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignMapper campaignMapper;
    private final MemberMapper memberMapper;
    private final CouponMapper couponMapper;

    @Operation(summary = "직원 - 캠페인 등록")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterCampaignVO> createCampaign
            (@RequestBody RequestRegisterCampaignVO requestCampaign) {
        List<MemberDTO> studentDTOList = requestCampaign.getStudentList().stream()
                .map(memberMapper::fromRequestFindCampaignStudentVOToMemberDTO)
                .toList();

        List<CouponDTO> couponDTOList = requestCampaign.getCouponList().stream()
                .map(couponMapper::fromRequestFindCampaignCouponVOToCouponDTO)
                .toList();

        CampaignDTO campaignDTO = campaignService.registerCampaign(campaignMapper
                .fromRegisterRequestVOtoDTO(requestCampaign)
                , studentDTOList
                , couponDTOList);

        return ResponseEntity.status(HttpStatus.CREATED).body(campaignMapper.fromDtoToRegisterResponseVO(campaignDTO));
    }

    @Operation(summary = "직원 - 예약된 캠페인 수정")
    @PutMapping("/edit")
    public ResponseEntity<ResponseEditCampaignVO> updateCampaign
            (@RequestBody RequestEditCampaignVO requestCampaign) {
        List<MemberDTO> studentDTOList = requestCampaign.getStudentList().stream()
                .map(memberMapper::fromRequestEditCampaignStudentVOToMemberDTO)
                .toList();

        List<CouponDTO> couponDTOList = requestCampaign.getCouponList().stream()
                .map(couponMapper::fromRequestEditCampaignCouponVOToCouponDTO)
                .toList();

        CampaignDTO campaignDTO = campaignService.editCampaign(campaignMapper
                .fromEditRequestVOtoDTO(requestCampaign)
                , studentDTOList
                , couponDTOList);

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
        List<FindAllCampaignDTO> findAllCampaignDTOList = campaignService.findAllCampaignList();
        List<ResponseFindCampaignVO> responseFindCampaignVOList = campaignMapper
                .fromDtoListToFindCampaignVO(findAllCampaignDTOList);

        return new ResponseEntity<>(responseFindCampaignVOList, HttpStatus.OK);
    }

    @Operation(summary = "직원 - 캠페인 단건 조회")
    @GetMapping("/{campaignCode}")
    public ResponseEntity<ResponseFindCampaignVO> getCampaign
            (@PathVariable Long campaignCode) {
        CampaignDTO getCampaignCode = new CampaignDTO();
        getCampaignCode.setCampaignCode(campaignCode);
        CampaignDTO campaignDTO = campaignService.findCampaign(getCampaignCode);

        return ResponseEntity.status(HttpStatus.OK).body(campaignMapper.fromDtoToFindResponseVO(campaignDTO));
    }

    @Operation(summary = "직원 - 조건 별 캠페인 조회")
    @GetMapping("/filter")
    public ResponseEntity<List<ResponseFindCampaignByConditionVO>> filterCampaigns
            (@RequestBody RequestFindCampaignByConditionVO requestCampaignList
                    , LocalDateTime startDate
                    , LocalDateTime endDate){
        CampaignDTO campaignDTO = campaignMapper.fromFindCampaignByConditionVOtoDTO(requestCampaignList);
        List<CampaignDTO> campaignDTOList = campaignService.findCampaignListByCondition(campaignDTO, startDate, endDate);

        List<ResponseFindCampaignByConditionVO> responseFindCampaignByConditionVOList = campaignMapper
                .fromDtoListToFindCampaignByConditionVO(campaignDTOList);

        return new ResponseEntity<>(responseFindCampaignByConditionVOList, HttpStatus.OK);
    }
}
