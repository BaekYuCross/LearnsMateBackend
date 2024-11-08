package intbyte4.learnsmate.coupon_category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "CouponCategory")
@Table(name = "coupon_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CouponCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_category_code", nullable = false, unique = true)
    private Integer couponCategoryCode;

    @Column(name = "coupon_category_name", nullable = false)
    private String couponCategoryName;
}
