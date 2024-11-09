package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.campaign.repository.CampaignRepository;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.couponbycampaign.service.CouponByCampaignService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.service.UserPerCampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final AdminService adminService;
    private final UserPerCampaignService userPerCampaignService;
    private final CouponByCampaignService couponByCampaignService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final CampaignMapper campaignMapper;

    @Override
    public CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList) {

        LocalDateTime sendTime;

        AdminDTO adminDTO = adminService.findByAdminCode(requestCampaign.getAdminCode());
        Admin admin = adminDTO.convertToEntity();

        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())){
            sendTime = LocalDateTime.now();
        } else {
            sendTime = requestCampaign.getCampaignSendDate();
        };

        Campaign campaign = Campaign.builder()
                .campaignCode(null)
                .campaignTitle(requestCampaign.getCampaignTitle())
                .campaignContents(requestCampaign.getCampaignContents())
                .campaignType(CampaignTypeEnum.valueOf(requestCampaign.getCampaignType()))
                .campaignSendDate(sendTime)
                .createdAt(requestCampaign.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .admin(admin)
                .build();

        campaignRepository.save(campaign);

        requestStudentList.forEach(memberDTO -> {
            MemberDTO foundStudent = memberService.findMemberByStudentCode(memberDTO.getMemberCode());
            if (foundStudent == null) throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);
            userPerCampaignService.registerUserPerCampaign(foundStudent, requestCampaign);
        });

        requestCouponList.forEach(couponDTO -> {
            CouponDTO foundCoupon = couponService.findCouponByCouponCode(couponDTO.getCouponCode());
            if (foundCoupon == null) throw new CommonException(StatusEnum.COUPON_NOT_FOUND);
            couponByCampaignService.registerCouponByCampaign(foundCoupon, requestCampaign);
        });

        return campaignMapper.toDTO(campaign);
    }

    @Override
    public CampaignDTO editCampaign(CampaignDTO request, Long campaignCode) {
        if (campaignCode == null) {
            throw new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND);
        }
        if (Objects.equals(request.getCampaignType(), CampaignTypeEnum.INSTANT.getType())){
            throw new CommonException(StatusEnum.UPDATE_NOT_ALLOWED);
        }

        AdminDTO adminDTO = adminService.findByAdminCode(request.getAdminCode());
        Admin admin = adminDTO.convertToEntity();

        Campaign updatedCampaign = campaignMapper.toEntity(request, admin);

        campaignRepository.save(updatedCampaign);

        return campaignMapper.toDTO(updatedCampaign);
    }

    @Override
    public void removeCampaign(CampaignDTO request){
        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        if(Objects.equals(request.getCampaignType(), CampaignTypeEnum.INSTANT.getType())){
            throw new CommonException(StatusEnum.DELETE_NOT_ALLOWED);
        }

        campaignRepository.delete(campaign);
    }

    @Override
    public List<CampaignDTO> findAllCampaigns(){
        List<Campaign> campaign = campaignRepository.findAll();
        List<CampaignDTO> campaignDTOList = new ArrayList<>();
        campaign.forEach(dto -> campaignDTOList.add(campaignMapper.toDTO(dto)));

        return campaignDTOList;
    }

    @Override
    public CampaignDTO findCampaign(CampaignDTO request){
        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        return campaignMapper.toDTO(campaign);
    }
}
