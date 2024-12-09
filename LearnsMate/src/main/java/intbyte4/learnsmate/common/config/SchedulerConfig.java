package intbyte4.learnsmate.common.config;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.repository.CampaignRepository;
import intbyte4.learnsmate.campaign.service.CampaignService;
import intbyte4.learnsmate.voc.service.VOCAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class SchedulerConfig {
    private final VOCAiService vocAiService;
    private final CampaignService campaignService;
    private final CampaignRepository campaignRepository;

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void scheduleVocAnalysis() {
        vocAiService.analyzeVocForLastWeek();
    }

    @Scheduled(cron = "0 0 */3 * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void scheduleCampaigns() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Campaign> readyCampaigns = campaignRepository
                .findByCampaignSendDateLessThanEqualAndCampaignSendFlagFalseAndCampaignType(
                        currentTime,
                        CampaignTypeEnum.RESERVATION
                );

        for (Campaign campaign : readyCampaigns) {
            try {
                campaignService.executeCampaign(campaign);
            } catch (Exception e) {
                log.error("Failed to execute scheduled campaign: {}", campaign.getCampaignCode(), e);
            }
        }
    }
}