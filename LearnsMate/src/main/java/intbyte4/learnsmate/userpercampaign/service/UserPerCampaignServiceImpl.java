package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import intbyte4.learnsmate.userpercampaign.mapper.UserPerCampaignMapper;
import intbyte4.learnsmate.userpercampaign.repository.UserPerCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPerCampaignServiceImpl implements UserPerCampaignService{
    private final UserPerCampaignRepository userPerCampaignRepository;
    private final UserPerCampaignMapper userPerCampaignMapper;

    @Override
    public void registerUserPerCampaign(MemberDTO member, CampaignDTO campaign) {
        Member getStudentCode = Member.builder()
                .memberCode(member.getMemberCode())
                .build();

        Campaign getCampaignCode = Campaign.builder()
                .campaignCode(campaign.getCampaignCode())
                .build();

        UserPerCampaign userPerCampaign = UserPerCampaign.builder()
                .userPerCampaignCode(null)
                .campaign(getCampaignCode)
                .student(getStudentCode)
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
}
