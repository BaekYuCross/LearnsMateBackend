package intbyte4.learnsmate.campaign.domain.vo.request;

import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestInsertCampaignVO {
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private LocalDateTime campaignSendDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;

    public CampaignDTO toDTO() {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignCode(this.getCampaignCode());
        campaignDTO.setCampaignTitle(this.getCampaignTitle());
        campaignDTO.setCampaignContents(this.getCampaignContents());
        campaignDTO.setCampaignType(String.valueOf(this.campaignType));
        campaignDTO.setCampaignSendDate(this.getCampaignSendDate());
        campaignDTO.setCreatedAt(this.getCreatedAt());
        campaignDTO.setUpdatedAt(this.getUpdatedAt());
        campaignDTO.setAdminCode(this.getAdminCode());
        return campaignDTO;
    }
}
