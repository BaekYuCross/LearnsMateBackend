package intbyte4.learnsmate.campaign_template.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RequestFindCampaignTemplateByFilterVO {
    @JsonProperty("campaign_template_title")
    private String campaignTemplateTitle;

    @JsonProperty("campaign_template_start_post_date")
    private LocalDateTime campaignTemplateStartPostDate;

    @JsonProperty("campaign_template_end_post_date")
    private LocalDateTime campaignTemplateEndPostDate;

    @JsonProperty("admin_code")
    private String adminCode;

    @JsonProperty("admin_name")
    private String adminName;
}
