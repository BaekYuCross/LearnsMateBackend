package intbyte4.learnsmate.campaign.domain.vo.response;

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
public class ResponseInsertCampaignVO {
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private LocalDateTime campaignSendDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long adminCode;

    public ResponseInsertCampaignVO fromDTO(CampaignDTO campaignDTO){
        ResponseInsertCampaignVO responseInsertCampaignVO = new ResponseInsertCampaignVO();
        responseInsertCampaignVO.setCampaignCode(campaignDTO.getCampaignCode());
        responseInsertCampaignVO.setCampaignTitle(campaignDTO.getCampaignTitle());
        responseInsertCampaignVO.setCampaignContents(campaignDTO.getCampaignContents());
        responseInsertCampaignVO.setCampaignType(campaignDTO.getCampaignType());
        responseInsertCampaignVO.setCampaignSendDate(campaignDTO.getCampaignSendDate());
        responseInsertCampaignVO.setCreatedAt(campaignDTO.getCreatedAt());
        responseInsertCampaignVO.setUpdatedAt(campaignDTO.getUpdatedAt());
        responseInsertCampaignVO.setAdminCode(campaignDTO.getAdminCode());
        return responseInsertCampaignVO;
    }
}
