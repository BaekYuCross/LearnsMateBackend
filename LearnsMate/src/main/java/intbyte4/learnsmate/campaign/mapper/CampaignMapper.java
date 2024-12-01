package intbyte4.learnsmate.campaign.mapper;


import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign.domain.dto.*;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignByFilterVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestRegisterCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.*;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CampaignMapper {

    public CampaignDTO toDTO(Campaign entity) {
        return CampaignDTO.builder()
                .campaignCode(entity.getCampaignCode())
                .campaignTitle(entity.getCampaignTitle())
                .campaignContents(entity.getCampaignContents())
                .campaignType(String.valueOf(entity.getCampaignType()))
                .campaignMethod(entity.getCampaignMethod())
                .campaignSendDate(entity.getCampaignSendDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .adminCode(entity.getAdmin().getAdminCode())
                .build();
    }

    public Campaign toEntity(CampaignDTO dto, Admin admin) {
        return Campaign.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(CampaignTypeEnum.valueOf(dto.getCampaignType()))
                .campaignMethod(dto.getCampaignMethod())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .admin(admin)
                .build();
    }

    public CampaignDTO fromRegisterRequestVOtoDTO(RequestRegisterCampaignVO vo) {
        return CampaignDTO.builder()
                .campaignCode(vo.getCampaignCode())
                .campaignTitle(vo.getCampaignTitle())
                .campaignContents(vo.getCampaignContents())
                .campaignType(String.valueOf(vo.getCampaignType()))
                .campaignMethod(vo.getCampaignMethod())
                .campaignSendDate(vo.getCampaignSendDate())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .adminCode(vo.getAdminCode())
                .build();
    }

    public CampaignDTO fromFindCampaignByConditionVOtoDTO(RequestFindCampaignByFilterVO vo){
        return CampaignDTO.builder()
//                .campaignCode(vo.getCampaignCode())
                .campaignTitle(vo.getCampaignTitle())
//                .campaignContents(vo.getCampaignContents())
                .campaignType(String.valueOf(vo.getCampaignType()))
//                .campaignSendDate(vo.getCampaignSendDate())
//                .createdAt(vo.getCreatedAt())
//                .updatedAt(vo.getUpdatedAt())
//                .adminCode(vo.getAdminCode())
                .build();
    }

    public ResponseRegisterCampaignVO fromDtoToRegisterResponseVO(CampaignDTO dto){
        return ResponseRegisterCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .build();
    }

    public CampaignDTO fromEditRequestVOtoDTO(RequestEditCampaignVO vo) {
        return CampaignDTO.builder()
                .campaignCode(vo.getCampaignCode())
                .campaignTitle(vo.getCampaignTitle())
                .campaignContents(vo.getCampaignContents())
                .campaignSendDate(vo.getCampaignSendDate())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .adminCode(vo.getAdminCode())
                .campaignType(vo.getCampaignType())
                .build();
    }

    public ResponseEditCampaignVO fromDtoToEditResponseVO(CampaignDTO dto){
        return ResponseEditCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .build();
    }

    public CampaignPageResponse<ResponseFindCampaignVO> fromDtoListToFindCampaignVO(CampaignPageResponse<FindAllCampaignsDTO> dtoPage) {
        List<ResponseFindCampaignVO> voList = dtoPage.getContent().stream().map(dto -> ResponseFindCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .adminName(dto.getAdminName())
                .build()).toList();

        return new CampaignPageResponse<>(
                voList,                              // 변환된 VO 리스트
                dtoPage.getTotalElements(),          // 총 아이템 수
                dtoPage.getTotalPages(),             // 총 페이지 수
                dtoPage.getCurrentPage(),            // 현재 페이지 번호
                dtoPage.getSize()                    // 페이지 크기
        );
    }

    public ResponseFindCampaignVO fromDtoToFindResponseVO(CampaignDTO dto) {
        return ResponseFindCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .build();
    }

    public List<ResponseFindCampaignByFilterVO> fromDtoListToFindCampaignByConditionVO(List<CampaignDTO> dtoList) {
        return dtoList.stream().map(dto -> ResponseFindCampaignByFilterVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .build()).collect(Collectors.toList());
    }

    public List<ResponseFindCampaignByFilterVO> fromFindAllDtoListToFindCampaignByFilterVO(List<FindAllCampaignsDTO> dtoList) {
        return dtoList.stream().map(dto -> ResponseFindCampaignByFilterVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .adminCode(dto.getAdminCode())
                .adminName(dto.getAdminName())
                .build()).collect(Collectors.toList());
    }

    public FindAllCampaignsDTO toFindAllCampaignDTO(CampaignDTO campaignDTO, String adminName) {
        return FindAllCampaignsDTO.builder()
                .campaignCode(campaignDTO.getCampaignCode())
                .campaignTitle(campaignDTO.getCampaignTitle())
                .campaignContents(campaignDTO.getCampaignContents())
                .campaignType(campaignDTO.getCampaignType())
                .campaignSendDate(campaignDTO.getCampaignSendDate())
                .createdAt(campaignDTO.getCreatedAt())
                .updatedAt(campaignDTO.getUpdatedAt())
                .adminCode(campaignDTO.getAdminCode())
                .adminName(adminName)
                .build();
    }

    public FindAllCampaignsDTO fromCampaignToFindAllCampaignDTO(Campaign entity) {
        return FindAllCampaignsDTO.builder()
                .campaignCode(entity.getCampaignCode())
                .campaignTitle(entity.getCampaignTitle())
                .campaignContents(entity.getCampaignContents())
                .campaignType(String.valueOf(entity.getCampaignType()))
                .campaignSendDate(entity.getCampaignSendDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .adminCode(entity.getAdmin().getAdminCode())
                .adminName(entity.getAdmin().getAdminName())
                .build();
    }

    public CampaignFilterDTO fromFindCampaignByConditionVOtoFilterDTO(RequestFindCampaignByFilterVO request) {
        return CampaignFilterDTO.builder()
//                .campaignCode(request.getCampaignCode())
                .campaignTitle(request.getCampaignTitle())
//                .campaignContents(request.getCampaignContents())
                .campaignType(request.getCampaignType())
//                .campaignSendDate(request.getCampaignSendDate())
                .campaignStartPostDate(request.getCampaignStartPostDate())
                .campaignEndPostDate(request.getCampaignEndPostDate())
                .campaignStatus(request.getCampaignStatus())
//                .createdAt(request.getCreatedAt())
//                .updatedAt(request.getUpdatedAt())
//                .adminCode(request.getAdminCode())
                .build();
    }

    public ResponseFindCampaignByFilterVO fromCampaignToResponseFindCampaignByConditionVO(Campaign vo) {
        return ResponseFindCampaignByFilterVO.builder()
                .campaignCode(vo.getCampaignCode())
                .campaignTitle(vo.getCampaignTitle())
                .campaignContents(vo.getCampaignContents())
                .campaignType(vo.getCampaignType().getType())
                .campaignSendDate(vo.getCampaignSendDate())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .adminCode(vo.getAdmin().getAdminCode())
                .adminName(vo.getAdmin().getAdminName())
                .build();
    }

    public ResponseFindCampaignVO fromFindCampaignDtoToFindResponseVO(FindCampaignDTO dto) {
        return ResponseFindCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .members(dto.getMembers())
                .coupons(dto.getCoupons())
                .build();
    }

    public ResponseFindCampaignDetailVO fromFindCampaignDetailDtoToFindResponseVO(FindCampaignDetailDTO dto) {
        return ResponseFindCampaignDetailVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .members(dto.getMembers())
                .coupons(dto.getCoupons())
                .build();
    }


    public FindCampaignDetailDTO toFindCampaignDetailDTO(CampaignDTO campaignDTO, Page<CouponDTO> couponPage, Page<MemberDTO> memberPage) {
        return FindCampaignDetailDTO.builder()
                .campaignCode(campaignDTO.getCampaignCode())
                .campaignTitle(campaignDTO.getCampaignTitle())
                .campaignContents(campaignDTO.getCampaignContents())
                .campaignType(campaignDTO.getCampaignType())
                .campaignSendDate(campaignDTO.getCampaignSendDate())
                .createdAt(campaignDTO.getCreatedAt())
                .createdAt(campaignDTO.getUpdatedAt())
                .members(memberPage)
                .coupons(couponPage)
                .build();
    }
}
