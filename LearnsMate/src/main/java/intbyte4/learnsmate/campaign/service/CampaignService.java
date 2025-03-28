package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.campaign.domain.dto.*;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByFilterVO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignService {
    CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);

    void executeCampaign(Campaign campaign);

    void registerScheduledCampaign(List<MemberDTO> studentList, List<CouponDTO> couponList, LocalDateTime scheduledTime);

    @Transactional
    List<CampaignDTO> getReadyCampaigns(LocalDateTime currentTime);

    CampaignDTO editCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList);
    void removeCampaign(CampaignDTO request);

    CampaignPageResponse<FindAllCampaignsDTO> findAllCampaignList(int page, int size);

    CampaignPageResponse<FindAllCampaignsDTO> findAllCampaignListBySort(int page, int size, String sortField, String sortDirection);

    FindCampaignDetailDTO findCampaign(FindCampaignDetailDTO request, int page, int size);

    CampaignPageResponse<ResponseFindCampaignByFilterVO> findCampaignListByFilter
            (CampaignFilterDTO request, int page, int size);
    List<FindAllCampaignsDTO> findCampaignListByConditionWithExcel(CampaignFilterDTO filterDTO);
    List<FindAllCampaignsDTO> findAllCampaignListWithExcel();

    void updateCampaignSendFlag(Long campaignCode);

    CampaignPageResponse<ResponseFindCampaignByFilterVO> findCampaignListByFilterAndSort(CampaignFilterDTO dto, int page, int size, String sortField, String sortDirection);
}
