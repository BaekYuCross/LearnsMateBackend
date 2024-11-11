package intbyte4.learnsmate.coupon_by_lecture.domain;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "couponByLecture")
@Table(name = "coupon_by_lecture")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class CouponByLecture {

    @Id
    @Column(name = "coupon_by_lecture_code", nullable = false)
    private Long couponByLectureCode;

    @ManyToOne
    @JoinColumn(name = "coupon_code")
    private CouponEntity coupon;

    @ManyToOne
    @JoinColumn(name = "lecture_code")
    private Lecture lecture;

    public void updateCouponDetails(CouponEntity coupon) {
        this.coupon = coupon;
    }
}
