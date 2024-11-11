package intbyte4.learnsmate.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign.domain.entity.QCampaign;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public QCampaign qCampaign() { return QCampaign.campaign; }
}
