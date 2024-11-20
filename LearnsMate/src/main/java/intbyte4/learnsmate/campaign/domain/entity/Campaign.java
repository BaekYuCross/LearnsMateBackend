package intbyte4.learnsmate.campaign.domain.entity;

import intbyte4.learnsmate.admin.domain.entity.Admin;
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
    @Enumerated(EnumType.STRING)
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

}
