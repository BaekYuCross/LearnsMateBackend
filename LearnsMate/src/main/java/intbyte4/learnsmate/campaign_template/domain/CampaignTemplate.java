package intbyte4.learnsmate.campaign_template.domain;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity(name = "CampaignTemplate")
@Table(name = "campaign_template")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CampaignTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_template_code", nullable = false, unique = true)
    private Long campaignTemplateCode;

    @Column(name = "campaign_template_title", nullable = false)
    private String campaignTemplateTitle;

    @Column(name = "campaign_template_contents", nullable = false)
    private String campaignTemplateContents;

    @Column(name = "campaign_template_flag", nullable = false)
    @ColumnDefault("true")
    private Boolean campaignTemplateFlag;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;
}
