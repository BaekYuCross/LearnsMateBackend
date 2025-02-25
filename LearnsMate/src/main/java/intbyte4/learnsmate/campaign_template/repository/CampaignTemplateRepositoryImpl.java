package intbyte4.learnsmate.campaign_template.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.campaign_template.domain.CampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.QCampaignTemplate;
import intbyte4.learnsmate.campaign_template.domain.dto.CampaignTemplateFilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Override
    public List<CampaignTemplate> searchByWithoutPaging(CampaignTemplateFilterDTO request) {
        return queryFactory.selectFrom(qCampaignTemplate)
                .where(searchByTitle(request)
                        .and(searchByPeriod(request))
                        .and(qCampaignTemplate.campaignTemplateFlag.eq(true)))
                .fetch();
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

    // 필터링o 정렬o
    @Override
    public Page<CampaignTemplate> searchByWithSort(CampaignTemplateFilterDTO campaignTemplateFilterDTO, Pageable pageable) {
        JPAQuery<CampaignTemplate> query = queryFactory.selectFrom(qCampaignTemplate)
                .where(searchByTitle(campaignTemplateFilterDTO)
                        .and(searchByPeriod(campaignTemplateFilterDTO)));

        // 정렬 필드에 따른 처리
        if (pageable.getSort().isSorted()) {
            Sort.Order order = pageable.getSort().iterator().next();
            OrderSpecifier<?> orderSpecifier = getOrderSpecifier(order);
            if (orderSpecifier != null) {
                query.orderBy(orderSpecifier);
            }
        }

        long total = query.fetchCount();
        List<CampaignTemplate> campaigns = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(campaigns, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Order order) {
        String property = order.getProperty();
        boolean isAsc = order.isAscending();

        switch (property) {
            case "campaignTemplateCode":
                return isAsc ? qCampaignTemplate.campaignTemplateCode.asc() : qCampaignTemplate.campaignTemplateCode.desc();
            case "campaignTemplateTitle":
                return isAsc ? qCampaignTemplate.campaignTemplateTitle.asc() : qCampaignTemplate.campaignTemplateTitle.desc();
            case "campaignTemplateContents":
                return isAsc ? qCampaignTemplate.campaignTemplateContents.asc() : qCampaignTemplate.campaignTemplateContents.desc();
            case "createdAt":
                return isAsc ? qCampaignTemplate.createdAt.asc() : qCampaignTemplate.createdAt.desc();
            case "updatedAt":
                return isAsc ? qCampaignTemplate.updatedAt.asc() : qCampaignTemplate.updatedAt.desc();
            case "adminName":
                return isAsc ? qCampaignTemplate.admin.adminName.asc() : qCampaignTemplate.admin.adminName.desc();
            default:
                return null;
        }
    }
}
