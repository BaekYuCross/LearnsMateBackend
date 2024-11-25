package intbyte4.learnsmate.issue_coupon.mapper;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponFilterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponFindResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponRegisterResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponUseResponseVO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.payment.domain.vo.RequestRegisterIssueCouponPaymentVO;
import org.springframework.stereotype.Component;

@Component
public class IssueCouponMapper {
    public IssueCouponDTO toDTO(IssueCoupon issueCoupon) {
        return IssueCouponDTO.builder()
                .couponIssuanceCode(issueCoupon.getCouponIssuanceCode())
                .couponIssueDate(issueCoupon.getCouponIssueDate())
                .couponUseStatus(issueCoupon.getCouponUseStatus())
                .couponUseDate(issueCoupon.getCouponUseDate())
                .studentCode(issueCoupon.getStudent().getMemberCode())
                .couponCode(issueCoupon.getCoupon().getCouponCode())
                .build();
    }

    public IssueCoupon toEntity(IssueCouponDTO dto, Member student, CouponEntity coupon) {
        return IssueCoupon.builder()
                .couponIssuanceCode(dto.getCouponIssuanceCode())
                .couponIssueDate(dto.getCouponIssueDate())
                .couponUseStatus(dto.getCouponUseStatus())
                .couponUseDate(dto.getCouponUseDate())
                .student(student)
                .coupon(coupon)
                .build();
    }

    public IssueCouponRegisterResponseVO fromDtoToRegisterResponseVO(IssueCouponDTO issueCoupon) {
        return IssueCouponRegisterResponseVO.builder()
                .couponIssuanceCode(issueCoupon.getCouponIssuanceCode())
                .couponIssueDate(issueCoupon.getCouponIssueDate())
                .build();
    }

    public IssueCouponFindResponseVO fromDtoToFindResponseVO(IssueCouponDTO issueCouponDTO) {
        return IssueCouponFindResponseVO.builder()
                .couponIssuanceCode(issueCouponDTO.getCouponIssuanceCode())
                .couponIssueDate(issueCouponDTO.getCouponIssueDate())
                .build();
    }

    public IssueCouponUseResponseVO fromDtoToUseResponseVO(IssueCouponDTO usedCoupon) {
        return IssueCouponUseResponseVO.builder()
                .couponIssuanceCode(usedCoupon.getCouponIssuanceCode())
                .couponUseStatus(usedCoupon.getCouponUseStatus())
                .couponUseDate(usedCoupon.getCouponUseDate())
                .build();
    }

    public IssueCouponDTO fromRequestRegisterIssueCouponPaymentVOToDTO
            (RequestRegisterIssueCouponPaymentVO issueCouponVO) {
        return IssueCouponDTO.builder()
                .couponIssuanceCode(issueCouponVO.getCouponIssuanceCode())
                .couponIssueDate(issueCouponVO.getCouponIssueDate())
                .couponUseStatus(issueCouponVO.getCouponUseStatus())
                .couponUseDate(issueCouponVO.getCouponUseDate())
                .studentCode(issueCouponVO.getStudentCode())
                .couponCode(issueCouponVO.getCouponCode())
                .build();
    }

    public IssuedCouponFilterDTO fromVOToFilterResponseDTO(IssueCouponFilterRequestVO request) {
        return IssuedCouponFilterDTO.builder()
                .couponIssuanceCode(normalize(request.getCouponIssuanceCode()))
                .couponName(normalize(request.getCouponName()))
                .couponContents(normalize(request.getCouponContents()))
                .couponCategoryName(normalize(request.getCouponCategoryName()))
                .couponCategory(request.getCouponCategory())
                .studentCode(request.getStudentCode())
                .studentName(normalize(request.getStudentName()))
                .couponUseStatus(request.getCouponUseStatus())
                .minDiscountRate(request.getMinDiscountRate())
                .maxDiscountRate(request.getMaxDiscountRate())
                .startCouponStartDate(request.getStartCouponStartDate())
                .endCouponStartDate(request.getEndCouponStartDate())
                .startCouponExpireDate(request.getStartCouponExpireDate())
                .endCouponExpireDate(request.getEndCouponExpireDate())
                .startCouponIssueDate(request.getStartCouponIssueDate())
                .endCouponIssueDate(request.getEndCouponIssueDate())
                .build();
    }

    public IssueCouponFilterRequestVO fromDTOToFilterVO(IssuedCouponFilterDTO dto) {
        return IssueCouponFilterRequestVO.builder()
                .couponIssuanceCode(dto.getCouponIssuanceCode())
                .couponName(dto.getCouponName())
                .couponContents(dto.getCouponContents())
                .couponCategoryName(dto.getCouponCategoryName())
                .couponCategory(dto.getCouponCategory())
                .studentCode(dto.getStudentCode())
                .studentName(dto.getStudentName())
                .couponUseStatus(dto.getCouponUseStatus())
                .minDiscountRate(dto.getMinDiscountRate())
                .maxDiscountRate(dto.getMaxDiscountRate())
                .startCouponStartDate(dto.getStartCouponStartDate())
                .endCouponStartDate(dto.getEndCouponStartDate())
                .startCouponExpireDate(dto.getStartCouponExpireDate())
                .endCouponExpireDate(dto.getEndCouponExpireDate())
                .startCouponIssueDate(dto.getStartCouponIssueDate())
                .endCouponIssueDate(dto.getEndCouponIssueDate())
                .build();
    }

    private String normalize(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value;
    }
}
