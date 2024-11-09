package intbyte4.learnsmate.userpercampaign.mapper;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPerCampaignMapper {

    public UserPerCampaignDTO toDTO(UserPerCampaign entity) {
        return UserPerCampaignDTO.builder()
                .userPerCampaignCode(entity.getUserPerCampaignCode())
                .studentCode(entity.getStudent().getMemberCode())
                .campaignCode(entity.getCampaign().getCampaignCode())
                .build();
    }

    public UserPerCampaign toEntity(UserPerCampaignDTO dto, Member student, Campaign campaign) {
        return UserPerCampaign.builder()
                .userPerCampaignCode(dto.getUserPerCampaignCode())
                .student(student)
                .campaign(campaign)
                .build();
    }
}
