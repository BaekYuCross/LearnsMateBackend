package intbyte4.learnsmate.campaign_template.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CampaignTemplateFilterDTO {
    private Long campaignTemplateCode;
    private String campaignTemplateTitle;
    private String campaignTemplateContents;
    private LocalDateTime campaignTemplateStartPostDate;
    private LocalDateTime campaignTemplateEndPostDate;
    private Boolean campaignTemplateFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;
    private String adminName;
    private List<String> selectedColumns;
}
