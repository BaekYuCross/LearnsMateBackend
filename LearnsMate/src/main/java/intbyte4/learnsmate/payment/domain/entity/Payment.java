package intbyte4.learnsmate.payment.domain.entity;


import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "payment")
@Table(name = "payment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_code", nullable = false)
    private Long paymentCode;

    @Column(name = "payment_price", nullable = false)
    private Integer paymentPrice;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "lecture_by_student_code", nullable = false)
    private LectureByStudent lectureByStudent;

//    @ManyToOne
//    @JoinColumn(name = "coupon_issuance_code")
//    private IssueCoupon couponIssuance;


}
