package intbyte4.learnsmate.campaigntemplate.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class ResponseFindTemplateVO {
    @JsonProperty("campaign_template_code")
    private final Long campaignTemplateCode;

    @JsonProperty("campaign_template_title")
    private final String campaignTemplateTitle;

    @JsonProperty("campaign_template_contents")
    private final String campaignTemplateContents;

    @JsonProperty("campaign_template_flag")
    private final Boolean campaignTemplateFlag;

    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private final LocalDateTime updatedAt;

    @JsonProperty("admin_code")
    private final Long adminCode;
}
