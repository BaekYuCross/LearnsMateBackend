package intbyte4.learnsmate.coupon.mapper;

import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignCouponVO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponRegisterResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    public CouponDTO toDTO (CouponEntity entity) {
        return CouponDTO.builder()
                .couponCode(entity.getCouponCode())
                .couponName(entity.getCouponName())
                .couponContents(entity.getCouponContents())
                .couponDiscountRate(entity.getCouponDiscountRate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .couponStartDate(entity.getCouponStartDate())
                .couponExpireDate(entity.getCouponExpireDate())
                .couponCategoryCode(entity.getCouponCategoryCode())
                .adminCode(entity.getAdmin().getAdminCode())
                .tutorCode(entity.getTutor().getMemberCode())
                .build();
    }

    public CouponDTO fromCouponRegisterRequestVOToDTO(CouponRegisterRequestVO vo) {
        return CouponDTO.builder()
                .couponCode(vo.getCouponCode())
                .couponName(vo.getCouponName())
                .couponContents(vo.getCouponContents())
                .couponDiscountRate(vo.getCouponDiscountRate())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .couponStartDate(vo.getCouponStartDate())
                .couponExpireDate(vo.getCouponExpireDate())
                .couponFlag(true)
                .couponCategoryCode(vo.getCouponCategoryCode())
                .adminCode(vo.getAdminCode())
                .tutorCode(vo.getTutorCode())
                .build();
    }

    public CouponRegisterResponseVO fromDTOToRegisterResponseVO(CouponDTO couponDTO) {
        return CouponRegisterResponseVO.builder()
                .couponCode(couponDTO.getCouponCode())
                .couponName(couponDTO.getCouponName())
                .couponContents(couponDTO.getCouponContents())
                .couponDiscountRate(couponDTO.getCouponDiscountRate())
                .createdAt(couponDTO.getCreatedAt())
                .updatedAt(couponDTO.getUpdatedAt())
                .couponStartDate(couponDTO.getCouponStartDate())
                .couponExpireDate(couponDTO.getCouponExpireDate())
                .couponCategoryCode(couponDTO.getCouponCategoryCode())
                .adminCode(couponDTO.getAdminCode())
                .tutorCode(couponDTO.getTutorCode())
                .build();
    }

    public List<CouponFindResponseVO> fromDTOListToCouponFindVO(List<CouponDTO> couponDTOList) {
        return couponDTOList.stream().map(dto -> CouponFindResponseVO.builder()
                .couponCode(dto.getCouponCode())
                .couponName(dto.getCouponName())
                .couponContents(dto.getCouponContents())
                .couponDiscountRate(dto.getCouponDiscountRate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .couponStartDate(dto.getCouponStartDate())
                .couponExpireDate(dto.getCouponExpireDate())
                .couponCategoryCode(dto.getCouponCategoryCode())
                .adminCode(dto.getAdminCode())
                .tutorCode(dto.getTutorCode())
                .build()).collect(Collectors.toList());
    }

    public CouponFindResponseVO fromDTOToFindResponseVO(CouponDTO couponDTO) {
        return CouponFindResponseVO.builder()
                .couponCode(couponDTO.getCouponCode())
                .couponName(couponDTO.getCouponName())
                .couponContents(couponDTO.getCouponContents())
                .couponDiscountRate(couponDTO.getCouponDiscountRate())
                .createdAt(couponDTO.getCreatedAt())
                .updatedAt(couponDTO.getUpdatedAt())
                .couponStartDate(couponDTO.getCouponStartDate())
                .couponExpireDate(couponDTO.getCouponExpireDate())
                .couponCategoryCode(couponDTO.getCouponCategoryCode())
                .adminCode(couponDTO.getAdminCode())
                .tutorCode(couponDTO.getTutorCode())
                .build();
    }

    public CouponDTO fromRequestFindCampaignCouponVOToCouponDTO(RequestFindCampaignCouponVO request) {
        return CouponDTO.builder()
                .couponCode(request.getCouponCode())
                .couponName(request.getCouponName())
                .couponContents(request.getCouponContents())
                .couponDiscountRate(request.getCouponDiscountRate())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .couponStartDate(request.getCouponStartDate())
                .couponExpireDate(request.getCouponExpireDate())
                .couponFlag(true)
                .couponCategoryCode(request.getCouponCategoryCode())
                .adminCode(request.getAdminCode())
                .tutorCode(request.getTutorCode())
                .build();
    }
}
