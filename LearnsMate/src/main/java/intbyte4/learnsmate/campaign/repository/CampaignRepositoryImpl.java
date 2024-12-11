package intbyte4.learnsmate.campaign.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
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
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static intbyte4.learnsmate.campaign.domain.entity.QCampaign.campaign;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QCampaign qCampaign = campaign;

    @Override
    public Page<Campaign> searchBy(CampaignFilterDTO campaignFilterDTO, Pageable pageable) {

        JPAQuery<Campaign> query = queryFactory.selectFrom(qCampaign).where(searchByType(campaignFilterDTO)
                        .and(searchByTitle(campaignFilterDTO))
                        .and(searchByPeriod(campaignFilterDTO))
                        .and(searchBySendDateStatus(campaignFilterDTO)));

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(campaign, pageable.getSort());

        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        }

        long total = query.fetchCount();

        List<Campaign> campaigns = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return new PageImpl<>(campaigns, pageable, total);
    }

    @Override
    public List<Campaign> searchByWithoutPaging(CampaignFilterDTO request) {
       return queryFactory.selectFrom(qCampaign).where(searchByType(request)
                        .and(searchByTitle(request))
                        .and(searchByPeriod(request))
                        .and(searchBySendDateStatus(request))).fetch();
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


    public BooleanBuilder searchBySendDateStatus(CampaignFilterDTO campaignFilterDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 시작 발송 날짜 조건
        if (campaignFilterDTO.getCampaignStartPostDate() != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.goe(campaignFilterDTO.getCampaignStartPostDate()));
        }

        // 종료 발송 날짜 조건
        if (campaignFilterDTO.getCampaignEndPostDate() != null) {
            booleanBuilder.and(qCampaign.campaignSendDate.loe(campaignFilterDTO.getCampaignEndPostDate()));
        }

        // 발송 상태에 따른 조건
        if ("completed".equalsIgnoreCase(campaignFilterDTO.getCampaignStatus())) {
            // 발송 완료: 현재 시간 이전
            booleanBuilder.and(qCampaign.campaignSendDate.loe(LocalDateTime.now()));
        } else if ("scheduled".equalsIgnoreCase(campaignFilterDTO.getCampaignStatus())) {
            // 발송 예정: 현재 시간 이후
            booleanBuilder.and(qCampaign.campaignSendDate.goe(LocalDateTime.now()));
        }

        return booleanBuilder;
    }

    private OrderSpecifier<?> getOrderSpecifier(QCampaign campaign, Sort sort){
        if (sort.isEmpty()) {
            return campaign.campaignCode.desc();
        }

        Sort.Order order = sort.iterator().next();
        switch (order.getProperty()){
            case "campaignCode":
                return order.isAscending() ? campaign.campaignCode.asc() : campaign.campaignCode.desc();
            case "campaignTitle":
                return order.isAscending() ? campaign.campaignTitle.asc() : campaign.campaignTitle.desc();
            case "campaignContents":
                return order.isAscending() ? campaign.campaignContents.asc() : campaign.campaignContents.desc();
            case "campaignType":
                return order.isAscending() ? campaign.campaignType.asc() : campaign.campaignType.desc();
            case "campaignSendDate":
                return order.isAscending() ? campaign.campaignSendDate.asc() : campaign.campaignSendDate.desc();
            case "createdAt":
                return order.isAscending() ? campaign.createdAt.asc() : campaign.createdAt.desc();
            case "updatedAt":
                return order.isAscending() ? campaign.updatedAt.asc() : campaign.updatedAt.desc();
            case "adminName":
                return order.isAscending() ? campaign.admin.adminName.asc() : campaign.admin.adminName.desc();
            default:
                return campaign.campaignCode.desc();
        }
    }

}
