package intbyte4.learnsmate.couponbycampaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryDTO;
import intbyte4.learnsmate.coupon_category.mapper.CouponCategoryMapper;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.couponbycampaign.domain.entity.CouponByCampaign;
import intbyte4.learnsmate.couponbycampaign.mapper.CouponByCampaignMapper;
import intbyte4.learnsmate.couponbycampaign.repository.CouponByCampaignRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponByCampaignServiceImpl implements CouponByCampaignService{
    private final CouponByCampaignRepository couponByCampaignRepository;
    private final CouponByCampaignMapper couponByCampaignMapper;
    private final CouponMapper couponMapper;
    private final AdminMapper adminMapper;
    private final AdminService adminService;
    private final CouponCategoryService couponCategoryService;
    private final CouponCategoryMapper couponCategoryMapper;
    private final CampaignMapper campaignMapper;

    @Override
    public void registerCouponByCampaign(CouponDTO couponDTO, CampaignDTO campaignDTO) {
        CouponCategoryDTO couponCategoryDTO = couponCategoryService.findDTOByCouponCategoryCode(couponDTO.getCouponCategoryCode());
        CouponCategory couponCategory = couponCategoryMapper.toEntity(couponCategoryDTO);
        AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        CouponEntity couponEntity = couponMapper.toAdminCouponEntity(couponDTO, couponCategory, admin);

        Campaign campaign = campaignMapper.toEntity(campaignDTO, admin);

        CouponByCampaign couponByCampaign = CouponByCampaign.builder()
                .couponByCampaignCode(null)
                .coupon(couponEntity)
                .campaign(campaign)
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

    @Transactional
    @Override
    public void removeByCampaignCode(Long campaignCode) {
        couponByCampaignRepository.deleteByCampaign_CampaignCode(campaignCode);
    }
}
