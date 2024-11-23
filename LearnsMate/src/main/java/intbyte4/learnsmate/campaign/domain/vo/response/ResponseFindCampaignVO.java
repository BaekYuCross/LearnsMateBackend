package intbyte4.learnsmate.campaign.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseFindCampaignVO {

    // 캠페인 정보
    @JsonProperty("campaign_code")
    private Long campaignCode;

    @JsonProperty("campaign_title")
    private String campaignTitle;

    @JsonProperty("campaign_contents")
    private String campaignContents;

    @JsonProperty("campaign_type")
    private String campaignType;

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
    private List<MemberDTO> members;

    // 첨부된 쿠폰 정보
    private List<CouponDTO> coupons;

//    // 타겟 유저 정보
//    @JsonProperty("member_code")
//    private Long memberCode;
//
//    @JsonProperty("member_type")
//    private MemberType memberType;
//
//    @JsonProperty("member_email")
//    private String memberEmail;
//
//    @JsonProperty("member_password")
//    private String memberPassword;
//
//    @JsonProperty("member_name")
//    private String memberName;
//
//    @JsonProperty("member_age")
//    private Integer memberAge;
//
//    @JsonProperty("member_phone")
//    private String memberPhone;
//
//    @JsonProperty("member_address")
//    private String memberAddress;
//
//    @JsonProperty("member_birth")
//    private LocalDateTime memberBirth;
//
//    @JsonProperty("member_flag")
//    private Boolean memberFlag;
//
//    @JsonProperty("member_dormant_status")
//    private Boolean memberDormantStatus;
//
//    // 첨부된 쿠폰 정보
//    @JsonProperty("coupon_code")
//    private Long couponCode;
//
//    @JsonProperty("coupon_name")
//    private String couponName;
//
//    @JsonProperty("coupon_contents")
//    private String couponContents;
//
//    @JsonProperty("coupon_discount_rate")
//    private int couponDiscountRate;
//
//    @JsonProperty("coupon_start_date")
//    private LocalDateTime couponStartDate;
//
//    @JsonProperty("coupon_expire_date")
//    private LocalDateTime couponExpireDate;
//
//    @JsonProperty("coupon_flag")
//    private Boolean couponFlag;
//
//    @JsonProperty("coupon_category_code")
//    private int couponCategoryCode;
//
//    @JsonProperty("tutor_code")
//    private Long tutorCode;
}
