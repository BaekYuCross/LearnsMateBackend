package intbyte4.learnsmate.campaign.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StepPerformanceListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Step 시작 시간: " + LocalDateTime.now());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Step 종료 시간: " + LocalDateTime.now());
        return ExitStatus.COMPLETED;
    }
}