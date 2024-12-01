package intbyte4.learnsmate.campaign.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseFindCampaignDetailVO {
    // 캠페인 정보
    @JsonProperty("campaign_code")
    private Long campaignCode;

    @JsonProperty("campaign_title")
    private String campaignTitle;

    @JsonProperty("campaign_contents")
    private String campaignContents;

    @JsonProperty("campaign_type")
    private String campaignType;

    @JsonProperty("campaign_method")
    private String campaignMethod;

    @JsonProperty("campaign_send_flag")
    private Boolean campaignSendFlag;

    @JsonProperty("campaign_send_date")
    private LocalDateTime campaignSendDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // 로그인한 직원 정보
    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("admin_name")
    private String adminName;

    // 타겟 유저 정보
    private Page<MemberDTO> members;

    // 첨부된 쿠폰 정보
    private Page<CouponDTO> coupons;
}
