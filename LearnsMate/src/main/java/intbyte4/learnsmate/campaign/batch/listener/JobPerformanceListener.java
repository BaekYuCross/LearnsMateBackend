package intbyte4.learnsmate.campaign.batch.listener;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobPerformanceListener implements JobExecutionListener {

    private final MeterRegistry meterRegistry;

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.nanoTime();
        log.info("Job [{}] 시작", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        String caseType = jobExecution.getJobParameters().getString("case", "unknown");

        Timer.builder("campaign_job_duration_seconds")
                .description("Duration of campaign batch job")
                .tag("case", caseType)
                .register(meterRegistry)
                .record(duration, TimeUnit.NANOSECONDS);

        log.info("Job [{}] 종료. 케이스: [{}], 소요 시간: {} ms",
                jobExecution.getJobInstance().getJobName(),
                caseType,
                TimeUnit.NANOSECONDS.toMillis(duration));
    }
}
