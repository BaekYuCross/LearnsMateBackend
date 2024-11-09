package intbyte4.learnsmate.userpercampaign.domain.entity;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "userPerCampaign")
@Table(name = "user_per_campaign")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class UserPerCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_per_campaign_code")
    private Long userPerCampaignCode;

    @ManyToOne
    @JoinColumn(name = "campaign_code", nullable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "student_code", nullable = false)
    private Member student;

}
