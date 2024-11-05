package intbyte4.learnsmate.campaign_template.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RequestEditTemplateVO {

    @JsonProperty("campaign_template_title")
    private final String campaignTemplateTitle;

    @JsonProperty("campaign_template_contents")
    private final String campaignTemplateContents;
}
