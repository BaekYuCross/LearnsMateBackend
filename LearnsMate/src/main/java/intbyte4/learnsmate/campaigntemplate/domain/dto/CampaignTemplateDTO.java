package intbyte4.learnsmate.campaigntemplate.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CampaignTemplateDTO {

    @JsonProperty("campaign_template_code")
    private Long campaignTemplateCode;

    @JsonProperty("campaign_template_title")
    private String campaignTemplateTitle;

    @JsonProperty("campaign_template_contents")
    private String campaignTemplateContents;

    @JsonProperty("campaign_template_flag")
    private Boolean campaignTemplateFlag;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("admin_code")
    private Long adminCode;
}
