package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import intbyte4.learnsmate.userpercampaign.mapper.UserPerCampaignMapper;
import intbyte4.learnsmate.userpercampaign.repository.UserPerCampaignRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPerCampaignServiceImpl implements UserPerCampaignService{
    private final UserPerCampaignRepository userPerCampaignRepository;
    private final UserPerCampaignMapper userPerCampaignMapper;
    private final MemberMapper memberMapper;
    private final CampaignMapper campaignMapper;
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Override
    public void registerUserPerCampaign(MemberDTO memberDTO, CampaignDTO campaignDTO) {
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);

        AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        Campaign campaign = campaignMapper.toEntity(campaignDTO, admin);

        UserPerCampaign userPerCampaign = UserPerCampaign.builder()
                .userPerCampaignCode(null)
                .campaign(campaign)
                .student(member)
                .build();

        userPerCampaignRepository.save(userPerCampaign);
    }

    @Override
    public List<UserPerCampaignDTO> findByCampaignCode(Campaign campaign) {
        Campaign getCampaignCode = Campaign.builder()
                .campaignCode(campaign.getCampaignCode())
                .build();

        List<UserPerCampaign> userPerCampaignList = userPerCampaignRepository.findByCampaign(getCampaignCode);

        return userPerCampaignList.stream()
                .map(userPerCampaignMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeUserPerCampaign(Long userPerCampaignCode) {
        UserPerCampaign getUserPerCampaignCode = UserPerCampaign.builder()
                .userPerCampaignCode(userPerCampaignCode)
                .build();

        userPerCampaignRepository.delete(getUserPerCampaignCode);
    }

    @Transactional
    @Override
    public void removeByCampaignCode(Long campaignCode) {
        userPerCampaignRepository.deleteByCampaign_CampaignCode(campaignCode);
    }
}
