package intbyte4.learnsmate.campaign.mapper;


import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.dto.FindAllCampaignsDTO;
import intbyte4.learnsmate.campaign.domain.dto.FindCampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignByConditionVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestRegisterCampaignVO;
import intbyte4.learnsmate.campaign.domain.vo.response.*;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
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
                .campaignSendDate(vo.getCampaignSendDate())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .adminCode(vo.getAdminCode())
                .build();
    }

    public CampaignDTO fromFindCampaignByConditionVOtoDTO(RequestFindCampaignByConditionVO vo){
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

    public List<ResponseFindCampaignVO> fromDtoListToFindCampaignVO(List<FindAllCampaignsDTO> dtoList) {
        return dtoList.stream().map(dto -> ResponseFindCampaignVO.builder()
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

    public ResponseFindCampaignVO fromDtoToFindResponseVO(CampaignDTO dto) {
        return ResponseFindCampaignVO.builder()
                .campaignCode(dto.getCampaignCode())
                .campaignTitle(dto.getCampaignTitle())
                .campaignContents(dto.getCampaignContents())
                .campaignType(dto.getCampaignType())
                .campaignSendDate(dto.getCampaignSendDate())
                .build();
    }

    public List<ResponseFindCampaignByConditionVO> fromDtoListToFindCampaignByConditionVO(List<CampaignDTO> dtoList) {
        return dtoList.stream().map(dto -> ResponseFindCampaignByConditionVO.builder()
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

    public CampaignFilterDTO fromFindCampaignByConditionVOtoFilterDTO(RequestFindCampaignByConditionVO request) {
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

    public ResponseFindCampaignByConditionVO fromCampaignToResponseFindCampaignByConditionVO(Campaign vo) {
        return ResponseFindCampaignByConditionVO.builder()
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

    public FindCampaignDTO toFindCampaignDTO(CampaignDTO campaignDTO, List<CouponDTO> couponDTOList, List<MemberDTO> memberDTOList) {
        return FindCampaignDTO.builder()
                .campaignCode(campaignDTO.getCampaignCode())
                .campaignTitle(campaignDTO.getCampaignTitle())
                .campaignContents(campaignDTO.getCampaignContents())
                .campaignType(campaignDTO.getCampaignType())
                .campaignSendDate(campaignDTO.getCampaignSendDate())
                .createdAt(campaignDTO.getCreatedAt())
                .createdAt(campaignDTO.getUpdatedAt())
                .members(memberDTOList)
                .coupons(couponDTOList)
                .build();
    }
}
