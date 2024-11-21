package intbyte4.learnsmate.campaign.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.entity.QCampaign;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QCampaign qCampaign = QCampaign.campaign;

    @Override
    public Page<Campaign> searchBy(CampaignFilterDTO campaignFilterDTO, Pageable pageable) {

        JPAQuery<Campaign> query = queryFactory
                .selectFrom(qCampaign)
                .where(searchByType(campaignFilterDTO)
                        .and(searchByTitle(campaignFilterDTO))
                        .and(searchByPeriod(campaignFilterDTO))
                        .and(searchBySentStatus(campaignFilterDTO))
                        .and(searchByScheduledStatus(campaignFilterDTO))
                );

        long total = query.fetchCount();

        // 페이징 적용
        List<Campaign> campaigns = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(campaigns, pageable, total);
    }

    public BooleanBuilder searchByType(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignFilterDTO.getCampaignType() == null || campaignFilterDTO.getCampaignType().isEmpty()) {
            // type이 선택되지 않았을 경우, INSTANT와 RESERVATION 둘 다 포함
            booleanBuilder.and(qCampaign.campaignType.in(CampaignTypeEnum.INSTANT, CampaignTypeEnum.RESERVATION));
        } else {
            // type이 선택된 경우 해당 타입만 조회
            booleanBuilder.and(qCampaign.campaignType.eq(CampaignTypeEnum.valueOf(campaignFilterDTO.getCampaignType().toUpperCase())));
        }

        return booleanBuilder;
    }


    public BooleanBuilder searchByTitle(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignFilterDTO.getCampaignTitle() == null || campaignFilterDTO.getCampaignTitle().trim().isEmpty()) {
            return booleanBuilder; // 빈 제목은 아무 조건도 추가하지 않음
        }

        String[] keywords = campaignFilterDTO.getCampaignTitle().split(" ");
        for (String keyword : keywords) {
            booleanBuilder.or(qCampaign.campaignTitle.containsIgnoreCase(keyword));
        }

        return booleanBuilder;
    }


    public BooleanBuilder searchByPeriod(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignFilterDTO.getCampaignStartPostDate() != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.goe(campaignFilterDTO.getCampaignStartPostDate()));
        }

        if (campaignFilterDTO.getCampaignEndPostDate() != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.loe(campaignFilterDTO.getCampaignEndPostDate()));
        }

        return booleanBuilder;
    }


    public BooleanBuilder searchBySentStatus(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaignFilterDTO.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.loe(LocalDateTime.now()));

        return booleanBuilder;
    }

    public BooleanBuilder searchByScheduledStatus(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (campaignFilterDTO.getCampaignSendDate() == null) return booleanBuilder;

        booleanBuilder.and(qCampaign.campaignSendDate.goe(LocalDateTime.now()));

        return booleanBuilder;
    }
}
