package intbyte4.learnsmate.campaign_template.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.QCampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CampaignTemplateRepositoryImpl implements CampaignTemplateRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QCampaignTemplate qCampaignTemplate = QCampaignTemplate.campaignTemplate;

    @Override
    public Page<CampaignTemplate> searchBy(CampaignTemplateFilterDTO campaignTemplateFilterDTO, Pageable pageable) {
        JPAQuery<CampaignTemplate> query = queryFactory.selectFrom(qCampaignTemplate)
                .where(searchByTitle(campaignTemplateFilterDTO)
                .and(searchByPeriod(campaignTemplateFilterDTO)));

        long total = query.fetchCount();

        // 페이징 적용
        List<CampaignTemplate> campaignTemplates = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return new PageImpl<>(campaignTemplates, pageable, total);
    }

    public BooleanBuilder searchByTitle(CampaignTemplateFilterDTO campaignTemplateFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignTemplateFilterDTO.getCampaignTemplateTitle() == null
                || campaignTemplateFilterDTO.getCampaignTemplateTitle().trim().isEmpty()) {
            return booleanBuilder; // 빈 제목은 아무 조건도 추가하지 않음
        }

        String[] keywords = campaignTemplateFilterDTO.getCampaignTemplateTitle().split(" ");
        for (String keyword : keywords) {
            booleanBuilder.or(qCampaignTemplate.campaignTemplateTitle.containsIgnoreCase(keyword));
        }

        return booleanBuilder;
    }


    public BooleanBuilder searchByPeriod(CampaignTemplateFilterDTO campaignTemplateFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (campaignTemplateFilterDTO.getCampaignTemplateStartPostDate() != null) {
            booleanBuilder.and(qCampaignTemplate.createdAt.goe(campaignTemplateFilterDTO.getCampaignTemplateStartPostDate()));
        }

        if (campaignTemplateFilterDTO.getCampaignTemplateEndPostDate() != null) {
            booleanBuilder.and(qCampaignTemplate.createdAt.loe(campaignTemplateFilterDTO.getCampaignTemplateEndPostDate()));
        }

        return booleanBuilder;
    }
}
