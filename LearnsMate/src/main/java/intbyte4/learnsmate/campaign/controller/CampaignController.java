package intbyte4.learnsmate.campaign.controller;

import intbyte4.learnsmate.campaign.domain.dto.*;
import intbyte4.learnsmate.campaign.domain.vo.request.*;
import intbyte4.learnsmate.campaign.domain.vo.response.*;
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

        log.info("requestCampaign:{}",requestCampaign);
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
    @PatchMapping("/edit")
    public ResponseEntity<ResponseEditCampaignVO> updateCampaign
            (@RequestBody RequestEditCampaignVO requestCampaign) {

        List<MemberDTO> studentDTOList = requestCampaign.getStudentList().stream()
                .map(memberMapper::fromRequestEditCampaignStudentVOToMemberDTO)
                .toList();
        log.info("studentDTOList {}", studentDTOList);
        List<CouponDTO> couponDTOList = requestCampaign.getCouponList().stream()
                .map(couponMapper::fromRequestEditCampaignCouponVOToCouponDTO)
                .toList();
        log.info("couponDTOList {}", couponDTOList);
        CampaignDTO campaignDTO = campaignService.editCampaign(campaignMapper
                .fromEditRequestVOtoDTO(requestCampaign)
                , studentDTOList
                , couponDTOList);
        log.info("campaignDTO  수정완료 {}", campaignDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignMapper.fromDtoToEditResponseVO(campaignDTO));
    }

    @Operation(summary = "직원 - 예약된 캠페인 취소")
    @DeleteMapping("/delete/{campaignCode}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable("campaignCode") Long campaignCode) {
        CampaignDTO getCampaignCode = new CampaignDTO();
        getCampaignCode.setCampaignCode(campaignCode);
        log.info("넘어온 캠페인 코드 : {}", campaignCode);
        campaignService.removeCampaign(getCampaignCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "직원 - 캠페인 전체 조회")
    @GetMapping("/campaigns")
    public ResponseEntity<CampaignPageResponse<ResponseFindCampaignVO>> getAllCampaigns(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "15") int size) {

        CampaignPageResponse<FindAllCampaignsDTO> findAllCampaignsDTOList = campaignService.findAllCampaignList(page, size);
        CampaignPageResponse<ResponseFindCampaignVO> response = campaignMapper
                .fromDtoListToFindCampaignVO(findAllCampaignsDTOList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - 캠페인 단건 조회")
    @GetMapping("/{campaignCode}")
    public ResponseEntity<ResponseFindCampaignDetailVO> getCampaignDetails
            (@PathVariable Long campaignCode,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "15") int size) {
        FindCampaignDetailDTO findCampaignDetailDTO = new FindCampaignDetailDTO();
        findCampaignDetailDTO.setCampaignCode(campaignCode);
        FindCampaignDetailDTO response = campaignService.findCampaign(findCampaignDetailDTO, page, size);
        log.info("campaign단건 조회: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(campaignMapper.fromFindCampaignDetailDtoToFindResponseVO(response));
    }

    @Operation(summary = "직원 - 조건 별 캠페인 조회")
    @PostMapping("/filter")
    public ResponseEntity<CampaignPageResponse<ResponseFindCampaignByFilterVO>> filterCampaigns
            (@RequestBody RequestFindCampaignByFilterVO request,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "15") int size){
        CampaignFilterDTO dto =
                campaignMapper.fromFindCampaignByConditionVOtoFilterDTO(request);
        log.info("반환된 조건 별 캠페인 : {}", dto);
        CampaignPageResponse<ResponseFindCampaignByFilterVO> response = campaignService
                .findCampaignListByFilter(dto, page, size);

        return ResponseEntity.ok(response);
    }
}
