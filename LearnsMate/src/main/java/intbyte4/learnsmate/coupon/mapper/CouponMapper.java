package intbyte4.learnsmate.coupon.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignCouponVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignCouponVO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.AdminCouponEditRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponEditRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.response.*;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
                .couponCategoryCode(entity.getCouponCategory().getCouponCategoryCode())
                .adminCode(entity.getAdmin().getAdminCode())
                .tutorCode(entity.getTutor().getMemberCode())
                .build();
    }

    public CouponEntity toEntity (CouponDTO dto, CouponCategory category, Admin admin, Member tutor) {
        return CouponEntity.builder()
                .couponCode(dto.getCouponCode())
                .couponName(dto.getCouponName())
                .couponContents(dto.getCouponContents())
                .couponDiscountRate(dto.getCouponDiscountRate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .couponStartDate(dto.getCouponStartDate())
                .couponExpireDate(dto.getCouponExpireDate())
                .couponCategory(category)
                .admin(admin)
                .tutor(tutor)
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

    public List<CouponFilterResponseVO> fromDTOToCouponFilterResponseVO(List<CouponDTO> couponDTOList) {
        return couponDTOList.stream().map(dto -> CouponFilterResponseVO.builder()
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

    public CouponDTO fromRequestEditCampaignCouponVOToCouponDTO(RequestEditCampaignCouponVO request) {
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

    public CouponEntity newCouponEntity(TutorCouponRegisterRequestVO request, Member tutor, CouponCategory couponCategory) {
        return CouponEntity.builder()
                .couponCode(request.getCouponCode())
                .couponName(request.getCouponName())
                .couponDiscountRate(request.getCouponDiscountRate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .couponStartDate(LocalDateTime.now())
                .couponExpireDate(request.getCouponExpireDate())
                .couponFlag(true)
                .couponCategory(couponCategory)
                .tutor(tutor)
                .build();
    }

    public CouponDTO fromEditRequestVOToDto(AdminCouponEditRequestVO request) {
        return CouponDTO.builder()
                .couponName(request.getCouponName())
                .couponContents(request.getCouponContents())
                .couponDiscountRate(request.getCouponDiscountRate())
                .couponStartDate(request.getCouponStartDate())
                .couponExpireDate(request.getCouponExpireDate())
                .build();
    }

    public CouponDTO fromTutorEditRequestVOToDTO(TutorCouponEditRequestVO request) {
        return CouponDTO.builder()
                .couponName(request.getCouponName())
                .couponStartDate(request.getCouponStartDate())
                .couponExpireDate(request.getCouponExpireDate())
                .couponDiscountRate(request.getCouponDiscountRate())
                .build();
    }

    public AdminCouponEditResponseVO fromDTOToEditResponseVO(CouponDTO updatedCouponDTO) {
        return AdminCouponEditResponseVO.builder()
                .couponName(updatedCouponDTO.getCouponName())
                .couponContents(updatedCouponDTO.getCouponContents())
                .couponDiscountRate(updatedCouponDTO.getCouponDiscountRate())
                .couponStartDate(updatedCouponDTO.getCouponStartDate())
                .couponExpireDate(updatedCouponDTO.getCouponExpireDate())
                .build();
    }

    public TutorCouponEditResponseVO fromTutorDTOToResponse(CouponDTO updatedCouponDTO) {
        return TutorCouponEditResponseVO.builder()
                .couponName(updatedCouponDTO.getCouponName())
                .couponStartDate(updatedCouponDTO.getCouponStartDate())
                .couponExpireDate(updatedCouponDTO.getCouponExpireDate())
                .couponDiscountRate(updatedCouponDTO.getCouponDiscountRate())
                .build();
    }

    public CouponDTO fromEntityToDTO(CouponEntity updatedCoupon) {
        return CouponDTO.builder()
                .couponCode(updatedCoupon.getCouponCode())
                .couponName(updatedCoupon.getCouponName())
                .couponContents(updatedCoupon.getCouponContents())
                .couponDiscountRate(updatedCoupon.getCouponDiscountRate())
                .createdAt(updatedCoupon.getCreatedAt())
                .updatedAt(updatedCoupon.getUpdatedAt())
                .couponStartDate(updatedCoupon.getCouponStartDate())
                .couponExpireDate(updatedCoupon.getCouponExpireDate())
                .couponFlag(updatedCoupon.getCouponFlag())
                .couponCategoryCode(updatedCoupon.getCouponCategory().getCouponCategoryCode())
                .adminCode(updatedCoupon.getAdmin().getAdminCode())
                .build();
    }
}
