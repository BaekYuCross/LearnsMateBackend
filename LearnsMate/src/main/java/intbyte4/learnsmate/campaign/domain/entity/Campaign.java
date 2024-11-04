package intbyte4.learnsmate.campaign.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String campaignType;

    @Column(name = "campaign_send_date")
    private LocalDateTime campaignSendDate;

    @Column(name = "campaign_template_code")
    private Long campaignTemplateCode;

    @Column(name = "admin_code")
    private Long adminCode;

}
