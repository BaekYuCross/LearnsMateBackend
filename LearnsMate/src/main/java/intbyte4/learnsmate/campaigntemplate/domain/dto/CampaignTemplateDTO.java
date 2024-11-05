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

    private Long campaignTemplateCode;
    private String campaignTemplateTitle;
    private String campaignTemplateContents;
    private Boolean campaignTemplateFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;
}