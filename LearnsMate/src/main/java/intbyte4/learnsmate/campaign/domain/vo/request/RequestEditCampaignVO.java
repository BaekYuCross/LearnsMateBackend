package intbyte4.learnsmate.campaign.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestEditCampaignVO {
    private List<RequestEditCampaignStudentVO> studentList;
    private List<RequestEditCampaignCouponVO> couponList;

    @JsonProperty("campaign_title")
    private String campaignTitle;

    @JsonProperty("campaign_contents")
    private String campaignContents;

    @JsonProperty("campaign_send_date")
    private LocalDateTime campaignSendDate;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}
