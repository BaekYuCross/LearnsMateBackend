package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import intbyte4.learnsmate.userpercampaign.repository.UserPerCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPerCampaignServiceImpl implements UserPerCampaignService{
    private final UserPerCampaignRepository userPerCampaignRepository;

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
}
