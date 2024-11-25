package intbyte4.learnsmate.campaign.domain.dto;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class FindCampaignDTO {

    // 캠페인 정보
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private LocalDateTime campaignSendDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 로그인한 직원 정보
    private Long adminCode;
    private String adminName;

    // 타겟 유저 정보
    private List<MemberDTO> members;

    // 첨부된 쿠폰 정보
    private List<CouponDTO> coupons;
}
