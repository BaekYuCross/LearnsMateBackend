package intbyte4.learnsmate.common.config;

import intbyte4.learnsmate.campaign.batch.CampaignIssueCouponProcessor;
import intbyte4.learnsmate.campaign.batch.CampaignIssueCouponReader;
import intbyte4.learnsmate.campaign.batch.CampaignIssueCouponWriter;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final CampaignIssueCouponReader reader;
    private final CampaignIssueCouponProcessor processor;
    private final CampaignIssueCouponWriter writer;

    @Bean
    public Step couponProcessingStep() {
        return new StepBuilder("couponProcessingStep", jobRepository)
                .<Pair<MemberDTO, CouponDTO>, IssueCoupon>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job campaignJob(Step couponProcessingStep) {
        return new JobBuilder("campaignJob", jobRepository)
                .start(couponProcessingStep)
                .build();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }
}
