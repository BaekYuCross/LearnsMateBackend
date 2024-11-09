package intbyte4.learnsmate.couponbycampaign.domain.entity;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "couponByCampaign")
@Table(name = "coupon_by_campaign")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class CouponByCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_by_campaign_code")
    private Long couponByCampaignCode;

    @ManyToOne
    @JoinColumn(name = "coupon_code", nullable = false)
    private CouponEntity coupon;

    @ManyToOne
    @JoinColumn(name = "campaign_code", nullable = false)
    private Campaign campaign;
}
