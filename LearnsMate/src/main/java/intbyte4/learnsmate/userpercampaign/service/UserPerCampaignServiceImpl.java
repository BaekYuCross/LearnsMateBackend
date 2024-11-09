package intbyte4.learnsmate.userpercampaign.service;

import intbyte4.learnsmate.campaign.service.CampaignService;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.mapper.UserPerCampaignMapper;
import intbyte4.learnsmate.userpercampaign.repository.UserPerCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPerCampaignServiceImpl implements UserPerCampaignService{

    private final UserPerCampaignRepository userPerCampaignRepository;
    private final UserPerCampaignMapper userPerCampaignMapper;
    private final MemberService memberService;
    private final CampaignService campaignService;


    @Override
    public void registerUserPerCampaign(Object object) {

    }
}
