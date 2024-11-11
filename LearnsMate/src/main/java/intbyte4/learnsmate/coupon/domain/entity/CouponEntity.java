package intbyte4.learnsmate.coupon.domain.entity;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity(name = "Coupon")
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class CouponEntity {

    @Id
    @Column(name = "coupon_code", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponCode;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Column(name = "coupon_contents", nullable = false)
    private String couponContents;

    @Column(name = "coupon_discount_rate", nullable = false)
    private int couponDiscountRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "coupon_start_date", nullable = false)
    private LocalDateTime couponStartDate;

    @Column(name = "coupon_expire_date", nullable = false)
    private LocalDateTime couponExpireDate;

    @Column(name = "coupon_flag", nullable = false)
    private Boolean couponFlag;

    @ManyToOne
    @JoinColumn (name = "coupon_category_code", nullable = false)
    private CouponCategory couponCategory;

    @ManyToOne
    @JoinColumn (name = "admin_code")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "tutor_code")
    @Where(clause = "member_type = 'TUTOR'")
    private Member tutor;

    public void updateAdminCouponDetails(CouponDTO couponDTO) {
        this.couponName = couponDTO.getCouponName();
        this.couponContents = couponDTO.getCouponContents();
        this.couponDiscountRate = couponDTO.getCouponDiscountRate();
        this.couponStartDate = couponDTO.getCouponStartDate();
        this.couponExpireDate = couponDTO.getCouponExpireDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void deleteCoupon() {
        this.couponFlag = false;
    }
}
