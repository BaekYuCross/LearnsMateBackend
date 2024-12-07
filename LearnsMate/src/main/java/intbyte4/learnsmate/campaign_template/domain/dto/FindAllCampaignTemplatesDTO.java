package intbyte4.learnsmate.campaign_template.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FindAllCampaignTemplatesDTO {
    private Long campaignTemplateCode;
    private String campaignTemplateTitle;
    private String campaignTemplateContents;
    private Boolean campaignTemplateFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;
    private String adminName;
}
