package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.repository.CampaignRepository;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.common.mapper.CampaignMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final AdminService adminService;
    private final CampaignMapper campaignMapper;

    @Override
    public CampaignDTO registerCampaign(CampaignDTO request) {
        LocalDateTime sendTime;


        AdminDTO adminDTO = adminService.findByAdminCode(request.getAdminCode());
        Admin admin = adminDTO.convertToEntity();

        if (Objects.equals(request.getCampaignType(), CampaignTypeEnum.INSTANT.getType())){
            sendTime = LocalDateTime.now();
        } else {
            sendTime = request.getCampaignSendDate();
        };

        Campaign campaign = Campaign.builder()
                .campaignCode(null)
                .campaignTitle(request.getCampaignTitle())
                .campaignContents(request.getCampaignContents())
                .campaignType(CampaignTypeEnum.valueOf(request.getCampaignType()))
                .campaignSendDate(sendTime)
                .createdAt(request.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .admin(admin)
                .build();

        campaignRepository.save(campaign);

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
}
