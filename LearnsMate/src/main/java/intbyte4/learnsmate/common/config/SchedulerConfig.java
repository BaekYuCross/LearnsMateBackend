package intbyte4.learnsmate.common.config;

import intbyte4.learnsmate.voc.service.VOCAiService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final VOCAiService vocAiService;

    public SchedulerConfig(VOCAiService vocAiService) {
        this.vocAiService = vocAiService;
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void scheduleVocAnalysis() {
        vocAiService.analyzeVocForLastWeek();
    }
}
