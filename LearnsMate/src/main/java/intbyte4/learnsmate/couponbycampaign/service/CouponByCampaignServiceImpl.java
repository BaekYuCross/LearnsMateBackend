package intbyte4.learnsmate.couponbycampaign.service;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;
import intbyte4.learnsmate.couponbycampaign.mapper.CouponByCampaignMapper;
import intbyte4.learnsmate.couponbycampaign.repository.CouponByCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponByCampaignServiceImpl implements CouponByCampaignService{
    private final CouponByCampaignRepository couponByCampaignRepository;
    private final CouponByCampaignMapper couponByCampaignMapper;

    @Override
    public void registerCouponByCampaign(CouponDTO coupon, CampaignDTO campaign) {
        CouponEntity getCouponCode = CouponEntity.builder()
                .couponCode(coupon.getCouponCode())
                .build();

        Campaign getCampaignCode = Campaign.builder()
                .campaignCode(campaign.getCampaignCode())
                .build();

        CouponByCampaign couponByCampaign = CouponByCampaign.builder()
                .couponByCampaignCode(null)
                .coupon(getCouponCode)
                .campaign(getCampaignCode)
                .build();

        couponByCampaignRepository.save(couponByCampaign);
    }

    @Override
    public List<CouponByCampaignDTO> findByCampaignCode(Campaign campaign) {
        Campaign getCampaignCode = Campaign.builder()
                .campaignCode(campaign.getCampaignCode())
                .build();

        List<CouponByCampaign> couponByCampaignList = couponByCampaignRepository.findByCampaign(getCampaignCode);

        return couponByCampaignList.stream()
                .map(couponByCampaignMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCouponByCampaign(Long couponByCampaignCode) {
        CouponByCampaign getCouponByCampaignCode = CouponByCampaign.builder()
                .couponByCampaignCode(couponByCampaignCode)
                .build();

        couponByCampaignRepository.delete(getCouponByCampaignCode);
    }
}
