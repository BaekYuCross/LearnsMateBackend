package intbyte4.learnsmate.campaign.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestRegisterCampaignVO {

    @JsonProperty("student_list")
    private List<RequestFindCampaignStudentVO> studentList;

    @JsonProperty("coupon_list")
    private List<RequestFindCampaignCouponVO> couponList;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("campaign_send_date")
    private LocalDateTime campaignSendDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("admin_code")
    private Long adminCode;

}
