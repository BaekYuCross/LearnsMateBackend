package intbyte4.learnsmate.coupon.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

//@Entity(name = "Coupon")
//@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class CouponEntity {

    @Id
    @Column(name = "coupon_code")
    private Long couponCode;

    @Column(name = "coupon_name")
    private String couponName;

    @Column(name = "coupon_contents")
    private String couponContents;

    @Column(name = "coupon_discount_rate")
    private int couponDiscountRate;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "coupon_flag")
    private Boolean couponFlag;

    @Column(name = "campaign_code")
    private Long campaignCode;

    @Column(name = "admin_code")
    private Long adminCode;

    @Column(name=" tutor_code")
    private Long tutorCode;
}
