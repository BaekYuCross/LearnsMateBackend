package intbyte4.learnsmate.campaigntemplate.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "campaign_template_title", nullable = false, unique = true)
    private String campaignTemplateTitle;

    @Column(name = "campaign_template_contents", nullable = false, unique = true)
    private String campaignTemplateContents;

    @Column(name = "campaign_template_flag", nullable = false, unique = true)
    @ColumnDefault("true")
    private Boolean campaignTemplateFlag;

//    @ManyToOne
//    @JoinColumn(name = "admin_code", nullable = false)
//    private Admin admin;

    @Column(name = "admin_code", nullable = false)
    private Long adminCode;
}
