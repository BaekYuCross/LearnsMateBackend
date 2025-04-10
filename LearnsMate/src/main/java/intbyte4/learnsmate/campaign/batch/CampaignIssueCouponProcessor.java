package intbyte4.learnsmate.campaign.batch;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignIssueCouponProcessor implements ItemProcessor<Pair<MemberDTO, CouponDTO>, IssueCoupon> {

    private final MemberMapper memberMapper;
    private final CouponMapper couponMapper;
    private final CouponCategoryService couponCategoryService;
    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final Map<Integer, CouponCategory> couponCategoryCache = new ConcurrentHashMap<>();
    private final Map<Long, Admin> adminCache = new ConcurrentHashMap<>();

    @Override
    public IssueCoupon process(Pair<MemberDTO, CouponDTO> pair) throws Exception {
        MemberDTO studentDTO = pair.getLeft();
        CouponDTO couponDTO = pair.getRight();

        CouponCategory couponCategory = couponCategoryCache.computeIfAbsent(
                couponDTO.getCouponCategoryCode(),
                couponCategoryService::findByCouponCategoryCode
        );

        Admin admin = adminCache.computeIfAbsent(
                couponDTO.getAdminCode(),
                code -> {
                    AdminDTO adminDTO = adminService.findByAdminCode(code);
                    return adminMapper.toEntity(adminDTO);
                }
        );

        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);
        CouponEntity coupon = couponMapper.toAdminCouponEntity(couponDTO, couponCategory, admin);
        IssueCoupon issueCoupon = IssueCoupon.createIssueCoupon(coupon, student);

        return issueCoupon;
    }

}
