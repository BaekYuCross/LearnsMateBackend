package intbyte4.learnsmate.campaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom{
//    private final JPAQueryFactory queryFactory;

    @Override
    public List<Campaign> findAll() {
        return null;
    }
}
