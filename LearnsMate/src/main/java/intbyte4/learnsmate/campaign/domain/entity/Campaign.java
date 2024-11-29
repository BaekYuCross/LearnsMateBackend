package intbyte4.learnsmate.campaign.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime campaignSendDate;

    @Column(name="campaign_send_flag" , nullable = true)
    private Boolean campaignSendFlag;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;

    // 엔티티가 처음 저장되기 전에 실행되어 기본값을 설정해 줌
    @PrePersist
    public void prePersist() {
        if (campaignSendFlag == null) {
            campaignSendFlag = false;  // 기본값 설정
        }
    }

    public void updateSendFlag() {
        this.campaignSendFlag = true;
    }
}
