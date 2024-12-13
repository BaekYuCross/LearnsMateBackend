package intbyte4.learnsmate.campaign.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestFindCampaignByFilterVO {

    @JsonProperty("campaign_title")
    private String campaignTitle;

    @JsonProperty("campaign_type")
    private String campaignType;

    @JsonProperty("campaign_method")
    private String campaignMethod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("campaign_start_post_date")
    private LocalDateTime campaignStartPostDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("campaign_end_post_date")
    private LocalDateTime campaignEndPostDate;

    @JsonProperty("campaign_status")
    private String campaignStatus;

    @JsonProperty("admin_code")
    private Long adminCode;
}