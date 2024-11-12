package intbyte4.learnsmate.issue_coupon.mapper;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponFindResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponRegisterResponseVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.IssueCouponUseResponseVO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
}
