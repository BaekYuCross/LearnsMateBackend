package intbyte4.learnsmate.issue_coupon.domain;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity(name = "issueCoupon")
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
    private String couponIssuanceCode;

    @Column(name = "coupon_issue_date", nullable = false)
    private LocalDateTime couponIssueDate;

    @Column(name = "coupon_use_status", nullable = false)
    private Boolean couponUseStatus;

    @Column(name = "coupon_use_date")
    private LocalDateTime couponUseDate;

    @ManyToOne
    @JoinColumn(name = "student_code")
    private Member student;

    @ManyToOne
    @JoinColumn(name = "coupon_code")
    private CouponEntity coupon;

    public static IssueCoupon createIssueCoupon(CouponEntity coupon, Member student) {
        String couponIssuanceCode = generateCouponIssuanceCode(coupon);
        return IssueCoupon.builder()
                .couponIssuanceCode(couponIssuanceCode)
                .couponIssueDate(LocalDateTime.now())
                .couponUseStatus(false)
                .couponUseDate(null)
                .student(student)
                .coupon(coupon)
                .build();
    }

    private static String generateCouponIssuanceCode(CouponEntity coupon) {
        int couponCategoryCode = coupon.getCouponCategory().getCouponCategoryCode();
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueCode = UUID.randomUUID().toString().substring(0, 8);
        return String.format("C%s-%s%s", couponCategoryCode, formattedDate, uniqueCode);
    }

    public void useCoupon() {
        if (this.couponUseStatus) throw new CommonException(StatusEnum.COUPON_ALREADY_USED);
        this.couponUseStatus = true;
        this.couponUseDate = LocalDateTime.now();
    }
}
