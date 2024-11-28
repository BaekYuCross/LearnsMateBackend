package intbyte4.learnsmate.campaign.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CampaignFilterDTO {
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private LocalDateTime campaignSendDate;
    private LocalDateTime campaignStartPostDate;
    private LocalDateTime campaignEndPostDate;
    private String campaignStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;
    private String adminName;
    private List<String> selectedColumns;
}
