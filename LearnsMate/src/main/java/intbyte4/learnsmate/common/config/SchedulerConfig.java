package intbyte4.learnsmate.common.config;

import intbyte4.learnsmate.admin.service.EmailService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.service.CampaignService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.service.UserPerCampaignService;
import intbyte4.learnsmate.voc.service.VOCAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulerConfig {

    private final VOCAiService vocAiService;
    private final CampaignService campaignService;
    private final JobLauncher jobLauncher;
    private final Job campaignJob;
    private final MemberService memberService;
    private final EmailService emailService;
    private final UserPerCampaignService userPerCampaignService;

    public SchedulerConfig(VOCAiService vocAiService, CampaignService campaignService, JobLauncher jobLauncher, Job campaignJob, MemberService memberService, EmailService emailService , UserPerCampaignService userPerCampaignService) {
        this.vocAiService = vocAiService;
        this.campaignService = campaignService;
        this.jobLauncher = jobLauncher;
        this.campaignJob = campaignJob;
        this.memberService = memberService;
        this.emailService = emailService;
        this.userPerCampaignService = userPerCampaignService;
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void scheduleVocAnalysis() {
        vocAiService.analyzeVocForLastWeek();
    }

    @Scheduled(cron = "0 0 */3 * * *")
    public void scheduleCampaigns() {
        List<CampaignDTO> readyCampaigns = campaignService.getReadyCampaigns(LocalDateTime.now());
        for (CampaignDTO campaign : readyCampaigns) {
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("campaignCode", campaign.getCampaignCode())
                        .addDate("startTime", new Date())
                        .toJobParameters();
                jobLauncher.run(campaignJob, jobParameters);

                List<UserPerCampaignDTO> userPerCampaignDTOList = userPerCampaignService.findUserByCampaignCode(campaign.getCampaignCode());
                for (UserPerCampaignDTO userPerCampaignDTO : userPerCampaignDTOList) {
                    MemberDTO member = memberService.findMemberByMemberCode(userPerCampaignDTO.getStudentCode(), MemberType.STUDENT);
                    if (member != null) {
                        emailService.sendCampaignEmail(member.getMemberEmail(), campaign.getCampaignTitle(), campaign.getCampaignContents());
                    }
                }
                campaignService.updateCampaignSendFlag(campaign.getCampaignCode());
            } catch (Exception e) {
                System.err.println("Failed to launch campaign job: " + e.getMessage());
            }
        }
    }
}
