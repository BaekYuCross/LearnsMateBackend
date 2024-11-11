package intbyte4.learnsmate.campaign.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.QCampaign;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QCampaign qCampaign;

    @Override
    public List<Campaign> searchBy(Campaign campaign, LocalDateTime startDate, LocalDateTime endDate) {

        return queryFactory
                .selectFrom(qCampaign)
                .where(searchByType(campaign)
                        .and(searchByTitle(campaign))
                        .and(searchByPeriod(campaign, startDate, endDate))
                        .and(searchBySentStatus(campaign))
                        .and(searchByScheduledStatus(campaign))
                )
                .fetch();
    }

    public BooleanBuilder searchByType(Campaign campaign) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaign.getCampaignType() != null) {
            booleanBuilder.and(qCampaign.campaignType.eq(campaign.getCampaignType()));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchByTitle(Campaign campaign) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaign.getCampaignTitle() == null) {
            return booleanBuilder;
        }

        String[] keywords = campaign.getCampaignTitle().split(" ");
        if (keywords.length < 2) {
            throw new CommonException(StatusEnum.MINIMUM_KEYWORD_LENGTH_REQUIRED);
        }

        for (String keyword : keywords) {
            booleanBuilder.or(qCampaign.campaignTitle.containsIgnoreCase(keyword));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchByPeriod(Campaign campaign, LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaign.getCampaignSendDate() == null) return booleanBuilder;

        if (startDate != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.goe(startDate));
        }

        if (endDate != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.loe(endDate));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchBySentStatus(Campaign campaign) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaign.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.loe(LocalDateTime.now()));

        return booleanBuilder;
    }

    public BooleanBuilder searchByScheduledStatus(Campaign campaign) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaign.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.goe(LocalDateTime.now()));

        return booleanBuilder;
    }
}
