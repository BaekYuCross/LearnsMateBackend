package intbyte4.learnsmate.campaign.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestEditCampaignVO {
    private String campaignTitle;
    private String campaignContents;
    private LocalDateTime campaignSendDate;
    private LocalDateTime updatedAt;

}
