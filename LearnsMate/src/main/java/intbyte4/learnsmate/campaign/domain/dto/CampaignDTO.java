package intbyte4.learnsmate.campaign.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CampaignDTO {
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private String campaignMethod;
    private LocalDateTime campaignSendDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;

}
