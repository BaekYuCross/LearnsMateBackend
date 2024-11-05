package intbyte4.learnsmate.campaign.domain.entity;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "campaign")
@Table(name = "campaign")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="campaign_code")
    private Long campaignCode;

    @Column(name = "campaign_title")
    private String campaignTitle;

    @Column(name = "campaign_contents")
    private String campaignContents;

    @Column(name = "campaign_type")
    private CampaignTypeEnum campaignType;

    @Column(name = "campaign_send_date")
    private LocalDateTime campaignSendDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;

    public CampaignDTO toDTO() {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignCode(this.getCampaignCode());
        campaignDTO.setCampaignTitle(this.getCampaignTitle());
        campaignDTO.setCampaignContents(this.getCampaignContents());
        campaignDTO.setCampaignType(String.valueOf(this.campaignType));
        campaignDTO.setCampaignSendDate(this.getCampaignSendDate());
        campaignDTO.setCreatedAt(this.getCreatedAt());
        campaignDTO.setUpdatedAt(this.getUpdatedAt());
        campaignDTO.setAdminCode(this.admin.getAdminCode());
        return campaignDTO;
    }
}
