package intbyte4.learnsmate.campaign_template.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class ResponseEditTemplateVO {

    @JsonProperty("campaign_template_title")
    private final String campaignTemplateTitle;

    @JsonProperty("campaign_template_contents")
    private final String campaignTemplateContents;

    @JsonProperty("updated_at")
    private final LocalDateTime updatedAt;
}
