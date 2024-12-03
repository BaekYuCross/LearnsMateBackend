package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.campaign.domain.dto.FindCampaignDetailDTO;
import intbyte4.learnsmate.coupon.domain.dto.ClientFindCouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.pagination.CouponPageResponse;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {

    List<CouponDTO> findAllCoupons();

    CouponDTO findCouponDTOByCouponCode(Long couponCode);

    CouponEntity findByCouponCode(Long couponCode);

//    CouponDTO adminRegisterCoupon(AdminCouponRegisterRequestVO request, Admin admin);

    Page<CouponDTO> findCouponsByCampaignCode(FindCampaignDetailDTO campaignDTO, Pageable pageable);

    @Transactional
    CouponDTO adminRegisterCoupon(CouponDTO requestCoupon, List<String> lectureCodeList);

    CouponDTO editAdminCoupon(CouponDTO couponDTO);

    CouponDTO editTutorCoupon(CouponDTO couponDTO);

    CouponDTO deleteAdminCoupon(Long couponCode);

    CouponDTO tutorDeleteCoupon(Long couponCode);

    @Transactional
    CouponDTO tutorInactiveCoupon(Long couponCode);

    @Transactional
    CouponDTO tutorActivateCoupon(Long couponCode);

    @Transactional
    void saveCoupon(CouponEntity couponEntity);

    List<CouponEntity> filterCoupons(CouponFilterDTO dto);

    CouponPageResponse<CouponFindResponseVO> filterCoupons(CouponFilterDTO dto, int page, int size);

    List<ClientFindCouponDTO> findAllClientCoupon(Long tutorCode);
}
