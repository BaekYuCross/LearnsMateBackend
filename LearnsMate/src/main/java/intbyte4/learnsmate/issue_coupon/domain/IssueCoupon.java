package intbyte4.learnsmate.issue_coupon.domain;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity(name = "IssueCoupon")
@Table(name = "issue_coupon")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Where(clause = "member_type = 'STUDENT'")
public class IssueCoupon {

    @Id
    @Column(name = "coupon_issuance_code", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponIssuanceCode;

    @Column(name = "coupon_issue_date", nullable = false)
    private LocalDateTime couponIssueDate;

    @Column(name = "coupon_use_status", nullable = false)
    private Boolean couponUseStatus;

    @Column(name = "coupon_use_date")
    private LocalDateTime couponUseDate;

    @ManyToOne
    @JoinColumn (name = "student_code")
    private Member student;

    @ManyToOne
    @JoinColumn(name = "coupon_code")
    private CouponEntity coupon;
}
