package intbyte4.learnsmate.campaign.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.entity.QCampaign;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QCampaign qCampaign = QCampaign.campaign;

    @Override
    public List<Campaign> searchBy(CampaignDTO campaignDTO, LocalDateTime startDate, LocalDateTime endDate) {

        return queryFactory
                .selectFrom(qCampaign)
                .where(searchByType(campaignDTO)
                        .and(searchByTitle(campaignDTO))
                        .and(searchByPeriod(campaignDTO, startDate, endDate))
                        .and(searchBySentStatus(campaignDTO))
                        .and(searchByScheduledStatus(campaignDTO))
                )
                .fetch();
    }

    public BooleanBuilder searchByType(CampaignDTO campaignDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignDTO.getCampaignType() != null) {
            booleanBuilder.and(qCampaign.campaignType.eq(CampaignTypeEnum.valueOf(campaignDTO.getCampaignType())));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchByTitle(CampaignDTO campaignDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignDTO.getCampaignTitle() == null) {
            return booleanBuilder;
        }

        String[] keywords = campaignDTO.getCampaignTitle().split(" ");
        if (keywords.length < 2) {
            throw new CommonException(StatusEnum.MINIMUM_KEYWORD_LENGTH_REQUIRED);
        }

        for (String keyword : keywords) {
            booleanBuilder.or(qCampaign.campaignTitle.containsIgnoreCase(keyword));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchByPeriod(CampaignDTO campaignDTO, LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaignDTO.getCampaignSendDate() == null) return booleanBuilder;

        if (startDate != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.goe(startDate));
        }

        if (endDate != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.loe(endDate));
        }

        return booleanBuilder;
    }

    public BooleanBuilder searchBySentStatus(CampaignDTO campaignDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaignDTO.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.loe(LocalDateTime.now()));

        return booleanBuilder;
    }

    public BooleanBuilder searchByScheduledStatus(CampaignDTO campaignDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaignDTO.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.goe(LocalDateTime.now()));

        return booleanBuilder;
    }
}
