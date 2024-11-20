package intbyte4.learnsmate.campaign.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseFindCampaignVO {
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

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("admin_name")
    private String adminName;
}
